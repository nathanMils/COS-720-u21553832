package com.project.server.service;

import com.project.server.exception.InvalidTokenException;
import com.project.server.model.entity.*;
import com.project.server.model.enums.RoleEnum;
import com.project.server.model.projections.auth.UserAuthProjection;
import com.project.server.repository.*;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.ForgotPasswordRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.PasswordChangeRequest;
import com.project.server.response.auth.AuthResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.time.Instant;
import java.util.Arrays;


/**
 * Service class for handling authentication related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final OTPService otpService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ApplicationUDService userDetailsService;
    private final EmailService emailService;
    private final PasswordTokenService passwordTokenService;
    private final PasswordTokenRepository passwordTokenRepository;


    /**
     * Method for handling user application requests.
     *
     * @param servletRequest the HTTP request
     * @param request the application request
     */
    @Transactional
    public void apply(
            HttpServletRequest servletRequest,
            ApplicationRequest request
    ) {
        if (userRepository.existsByUsername(request.username())) {
            throw new EntityExistsException("USERNAME_EXISTS");
        }
        User user = userRepository.save(
                User.builder()
                        .username(request.username())
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .password(passwordEncoder.encode(request.password()))
                        .email(request.email())
                        .role(RoleEnum.ROLE_STUDENT)
                        .enabled(false)
                        .secret(otpService.generateSecret())
                        .build()
        );
        log.atInfo().log(
            "Student application submitted successfully: IP='{}' UserId='{}'",
            servletRequest.getRemoteAddr(),
            user.getId()
        );
        emailService.sendEmailVerificationEmail(
                user.getEmail(),
                confirmationTokenService.generateConfirmationToken(user)
        );
    }

    /**
     * Method for handling user login requests.
     *
     * @param servletRequest the HTTP request
     * @param request the login request
     * @return the authentication response
     */
    public AuthResponse login (
            HttpServletRequest servletRequest,
            LoginRequest request
    ) {
        UserAuthProjection user = userRepository.findUserProjectedByUsername(request.username()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS"));
        if (
                passwordEncoder.matches(request.password(), user.getPassword()) &&
                Boolean.TRUE.equals(user.getEnabled())
        ) {
            log.atInfo().log(
                "User login successful: IP='{}' Role='{}', UserId='{}'",
                servletRequest.getRemoteAddr(),
                user.getRole(),
                user.getId()
            );
            return buildAuthResponse(user);
        } else if (passwordEncoder.matches(request.password(), user.getPassword()) && Boolean.FALSE.equals(user.getEnabled())) {
            log.atInfo().log(
                "Unverified User login: IP='{}' Role='{}', UserId='{}'",
                servletRequest.getRemoteAddr(),
                user.getRole(),
                user.getId()
            );
            emailService.sendEmailVerificationEmail(
                    user.getEmail(),
                    confirmationTokenService.generateConfirmationToken(user)
            );
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "EMAIL_NOT_VERIFIED"
            );
        } else {
            log.atWarn().log(
                "User login failed: IP='{}' Role='{}', UserId='{}'",
                servletRequest.getRemoteAddr(),
                user.getRole(),
                user.getId()
            );
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_CREDENTIALS"
            );
        }
    }

    public void logout(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(cookie -> "accessToken".equals(cookie.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        Cookie newCookie = new Cookie(cookie.getName(), null);
                        newCookie.setMaxAge(0);
                        newCookie.setPath("/");
                        servletResponse.addCookie(newCookie);
                    });
            Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        refreshTokenService.revokeRefreshToken(cookie.getValue());
                        Cookie newCookie = new Cookie(cookie.getName(), null);
                        newCookie.setMaxAge(0);
                        newCookie.setPath("/");
                        servletResponse.addCookie(newCookie);
                    });
        }
    }

    /**
     * Method for handling token refresh requests.
     *
     * @param servletRequest the HTTP request
     * @return the authentication response
     */
    @Transactional
    public AuthResponse refresh(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null) {
            throw new InvalidTokenException();
        }

        Cookie refresh = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(InvalidTokenException::new);

        RefreshToken token = refreshTokenService.findByToken(refresh.getValue());
        if (token.isRevoked()) {
            Cookie newCookie = new Cookie(refresh.getName(), null);
            newCookie.setMaxAge(0);
            newCookie.setPath("/");
            servletResponse.addCookie(newCookie);
            throw new InvalidTokenException();
        } else if (token.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
            Cookie newCookie = new Cookie(refresh.getName(), null);
            newCookie.setMaxAge(0);
            newCookie.setPath("/");
            servletResponse.addCookie(newCookie);
            refreshTokenService.revokeRefreshToken(token.getToken());
            throw new InvalidTokenException();
        }
        return buildAuthResponse(userRepository.findUserProjectedById(token.getUserId()).orElseThrow(InvalidTokenException::new));
    }




    /**
     * Method for verifying the email verification code.
     *
     * @param servletRequest the HTTP request
     * @param uuid the verification token
     */
    public void verifyToken(
            HttpServletRequest servletRequest,
            String uuid
    ) {
        ConfirmationToken token = confirmationTokenService.getToken(uuid);
        if (token != null && token.getExpiryDate().compareTo(Date.from(Instant.now())) >= 0) {
            userRepository.findById(token.getUserId()).ifPresentOrElse(
                user -> {
                    log.atInfo().log(
                            "User email verification successful: IP='{}' Role='{}', UserId='{}'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    );
                    user.setEnabled(true);
                    userRepository.save(user);
                },
                () -> {
                    throw new InvalidTokenException();
                }
            );
        } else {
            log.atWarn().log(
                "Email verification failed: IP='{}' token:'{}'",
                servletRequest.getRemoteAddr(),
                uuid

            );
            throw new InvalidTokenException();
        }
    }

    /**
     * Method for handling password reset requests.
     *
     * @param servletRequest the HTTP request
     * @param request the password reset request
     */
    public void forgot(
            HttpServletRequest servletRequest,
            ForgotPasswordRequest request
    ) {
        UserAuthProjection user = userRepository.findUserProjectedByUsername(request.username()).orElse(null);
        if (user == null || !user.getEmail().equals(request.email()) || Boolean.FALSE.equals(user.getEnabled())) {
            log.atWarn().log(
                "Password reset failed: IP='{}' Username='{}'",
                servletRequest.getRemoteAddr(),
                request.username()
            );
            // Don't throw exception to prevent user enumeration
            return;
        }
        log.atInfo().log(
            "Password reset successful: IP='{}' Username='{}'",
            servletRequest.getRemoteAddr(),
            request.username()
        );
        emailService.sendPasswordResetEmail(
                user.getEmail(),
                passwordTokenService.createPasswordToken(user)
        );
    }

    /**
     * Method for verifying the validity of the password reset token.
     *
     * @param servletRequest the HTTP request
     * @param token the password reset token
     */
    public void isValid(HttpServletRequest servletRequest, String token) {
        PasswordToken passwordToken = passwordTokenService.getPasswordToken(token);
        if (passwordToken == null || passwordToken.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
            log.atWarn().log(
                "Password token invalid: IP='{}' token:'{}'",
                servletRequest.getRemoteAddr(),
                token
            );
            throw new InvalidTokenException();
        }
        log.atInfo().log(
            "Password token valid: IP='{}' token:'{}'",
            servletRequest.getRemoteAddr(),
            token
        );
    }

    /**
     * Method for handling password change requests.
     *
     * @param servletRequest the HTTP request
     * @param request the password change request
     */
    @Transactional
    public void changePassword(
            HttpServletRequest servletRequest,
            PasswordChangeRequest request
    ) {
        PasswordToken token = passwordTokenService.getPasswordToken(request.token());
        if (token != null && token.getExpiryDate().compareTo(Date.from(Instant.now())) >= 0) {
            User user = userRepository.findById(token.getUserId()).orElseThrow(InvalidTokenException::new);
            if (passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "PASSWORD_SAME"
                );
            }
            user.setPassword(passwordEncoder.encode(request.password()));
            userRepository.save(user);
            passwordTokenRepository.deleteByToken(request.token());
            log.atInfo().log(
                "Password reset successful: IP='{}' Role='{}', UserId='{}'",
                servletRequest.getRemoteAddr(),
                user.getRole(),
                user.getId()
            );
            return;
        } else if (token != null && token.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
            passwordTokenRepository.deleteByToken(request.token());
        }
        log.atWarn().log(
            "Password reset failed: IP='{}' token:'{}'",
            servletRequest.getRemoteAddr(),
            request.token()
        );
        throw new InvalidTokenException();
    }

    /**
     * Helper method for building the authentication response.
     *
     * @param user the user
     * @return the authentication response
     */
    private AuthResponse buildAuthResponse(UserAuthProjection user) {
        return AuthResponse.builder()
                .accessToken(
                        tokenService.genToken(user.getId(), user.getUsername())
                )
                .refreshToken(
                        refreshTokenService.createRefreshToken(user)
                )
                .build();
    }
}
