package com.project.server.service;

import com.project.server.model.entity.*;
import com.project.server.model.enums.RoleEnum;
import com.project.server.repository.*;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.ForgotPasswordRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.PasswordChangeRequest;
import com.project.server.response.auth.AuthResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;

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


    @Transactional
    public void apply(
            HttpServletRequest servletRequest,
            ApplicationRequest request
    ) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
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
                String.format(
                        "Student application submitted successfully: IP='%s' UserId='%s'",
                        servletRequest.getRemoteAddr(),
                        user.getId()
                )
        );
        emailService.sendEmailVerificationEmail(
                user.getEmail(),
                confirmationTokenService.generateConfirmationToken(user)
        );
    }

    public AuthResponse login (
            HttpServletRequest servletRequest,
            LoginRequest request
    ) {
        User user = userRepository.findByUsername(request.username()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS"));
        if (
                passwordEncoder.matches(request.password(), user.getPassword()) &&
                user.isEnabled()
        ) {
            log.atInfo().log(
                    String.format(
                            "User login successful: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    )
            );
            return buildAuthResponse(user);
        } else if (passwordEncoder.matches(request.password(), user.getPassword()) && !user.isEnabled()) {
            log.atInfo().log(
                    String.format(
                            "Unverified User login: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    )
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
                    String.format(
                            "User login failed: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    )
            );
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_CREDENTIALS"
            );
        }
    }

    @Transactional
    public boolean isLoggedIn(HttpServletRequest servletRequest) {
        try {
            Cookie[] cookies = servletRequest.getCookies();
            if (cookies == null) {
                return false;
            }

            String accessToken = getAccessTokenFromCookie(servletRequest);

            if (accessToken != null) {
                final String username = tokenService.getUserName(accessToken);
                if (username != null) {
                    UserDetails details = userDetailsService.loadUserByUsername(username);
                    return tokenService.validate(accessToken, details);
                }
            }
            return false;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    private String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Transactional
    public AuthResponse refresh(HttpServletRequest servletRequest) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID");
        }

        String refresh = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID"));

        RefreshToken token = refreshTokenService.verifyExpiration(refresh);
        if (token.isRevoked()) {
            log.atWarn().log(String.format("User refresh failed: IP='%s' Role='%s', UserId='%s'", servletRequest.getRemoteAddr(), token.getUser().getRole(), token.getUser().getId()));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID");
        }

        log.atInfo().log(String.format("User refresh successful: IP='%s' Role='%s', UserId='%s'", servletRequest.getRemoteAddr(), token.getUser().getRole(), token.getUser().getId()));
        return buildAuthResponse(token.getUser());
    }

    public void verifyToken(
            HttpServletRequest servletRequest,
            String uuid
    ) {
        ConfirmationToken token = confirmationTokenService.getToken(uuid);
        if (token != null && token.getExpiryDate().compareTo(Date.from(Instant.now())) >= 0) {
            User user = token.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            log.atInfo().log(
                    String.format(
                            "User email verification successful: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    )
            );
        } else {
            log.atWarn().log(
                    String.format(
                            "Email verification failed: IP='%s' token:'%s'",
                            servletRequest.getRemoteAddr(),
                            uuid
                    )
            );
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_TOKEN"
            );
        }
    }

    public void forgot(
            HttpServletRequest servletRequest,
            ForgotPasswordRequest request
    ) {
        User user = userRepository.findByUsername(request.username()).orElse(null);
        if (user == null || !user.getEmail().equals(request.email())) {
            // Dont throw exception to prevent user enumeration
            return;
        }
        emailService.sendPasswordResetEmail(
                user.getEmail(),
                passwordTokenService.createPasswordToken(user)
        );
    }

    public void isValid(HttpServletRequest servletRequest, String token) {
        PasswordToken passwordToken = passwordTokenService.getPasswordToken(token);
        if (passwordToken == null || passwordToken.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_TOKEN"
            );
        }
    }

    public void changePassword(
            HttpServletRequest servletRequest,
            PasswordChangeRequest request
    ) {
        PasswordToken token = passwordTokenService.getPasswordToken(request.token());
        if (token != null && token.getExpiryDate().compareTo(Date.from(Instant.now())) >= 0) {
            User user = token.getUser();
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
                    String.format(
                            "Password reset successful: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    )
            );
        } else if (token != null && token.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
            log.atWarn().log(
                    String.format(
                            "Password reset failed: IP='%s' token:'%s'",
                            servletRequest.getRemoteAddr(),
                            request.token()
                    )
            );
            passwordTokenRepository.deleteByToken(request.token());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_TOKEN"
            );
        } else {
            log.atWarn().log(
                    String.format(
                            "Password reset failed: IP='%s' token:'%s'",
                            servletRequest.getRemoteAddr(),
                            request.token()
                    )
            );
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_TOKEN"
            );
        }
    }

    private AuthResponse buildAuthResponse(User user) {
        return AuthResponse.builder()
                .accessToken(
                        tokenService.genToken(user)
                )
                .refreshToken(
                        refreshTokenService.createRefreshToken(user)
                )
                .build();
    }
}
