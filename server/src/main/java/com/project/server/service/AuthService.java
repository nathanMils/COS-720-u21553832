package com.project.server.service;

import com.project.server.exception.ConfirmationTokenException;
import com.project.server.exception.RefreshTokenException;
import com.project.server.model.dto.UserDTO;
import com.project.server.model.entity.*;
import com.project.server.model.enums.RoleEnum;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.*;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.RefreshRequest;
import com.project.server.response.auth.AuthResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final StudentApplicationRepository studentApplicationRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    private final OTPService otpService;
    private final ConfirmationTokenService confirmationTokenService;


    @Transactional
    public boolean apply(
            HttpServletRequest servletRequest,
            ApplicationRequest request
    ) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return false;
        }
        User user = userRepository.save(
                User.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .role(RoleEnum.ROLE_STUDENT)
                        .enabled(false)
                        .secret(otpService.generateSecret())
                        .build()
        );
        emailService.sendEmailVerificationEmail(
                user.getEmail(),
                confirmationTokenService.generateConfirmationToken(user)
        );
        log.atInfo().log(
                String.format(
                        "Student application submitted successfully: IP='%s' UserId='%s'",
                        servletRequest.getRemoteAddr(),
                        user.getId()
                )
        );
        return true;
    }

    public AuthResponse login (
            HttpServletRequest servletRequest,
            LoginRequest request
    ) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
        if (
                passwordEncoder.matches(request.getPassword(), user.getPassword()) &&
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
        } else if (passwordEncoder.matches(request.getPassword(), user.getPassword())){
            log.atInfo().log(
                    String.format(
                            "Unverified User login: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    )
            );
            String token = confirmationTokenService.generateConfirmationToken(user);
            emailService.sendEmailVerificationEmail(user.getEmail(), token);
            return AuthResponse.builder().verified(false).build();
        } else {
            log.atWarn().log(
                    String.format(
                            "User login failed: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    )
            );
            return null;
        }
    }

    @Transactional
    public AuthResponse refresh(
        HttpServletRequest servletRequest
    ) {
        if (servletRequest.getCookies() == null) {
            return null;
        }
        String refresh = null;
        for (Cookie cookie: servletRequest.getCookies()) {
            if (cookie.getName().equals("refreshToken")) {
                refresh = cookie.getValue();
                break;
            }
        }
        if (refresh == null) return null;
        RefreshToken token = refreshTokenService.verifyExpiration(refresh);
        if (token.isRevoked()) {
            log.atWarn().log(
                    String.format(
                            "User refresh failed: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            token.getUser().getRole(),
                            token.getUser().getId()
                    )
            );
            return null;
        }
        log.atInfo().log(
                String.format(
                        "User refresh successful: IP='%s' Role='%s', UserId='%s'",
                        servletRequest.getRemoteAddr(),
                        token.getUser().getRole(),
                        token.getUser().getId()
                )
        );
        return buildAuthResponse(token.getUser());
    }

    public boolean verifyToken(
            HttpServletRequest servletRequest,
            String uuid
    ) {
        ConfirmationToken token = confirmationTokenService.getToken(uuid);

        if (token != null) {
            User user = token.getUser();
            if (token.getExpiryDate().compareTo(Date.from(Instant.now())) >=0) {
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
                return true;
            }
            log.atWarn().log(
                    String.format(
                            "User email verification failed: IP='%s' Role='%s', UserId='%s'",
                            servletRequest.getRemoteAddr(),
                            user.getRole(),
                            user.getId()
                    )
            );
           return false;
        }
        log.atWarn().log(
                String.format(
                        "Unknown email verification token: IP='%s' token:'%s'",
                        servletRequest.getRemoteAddr(),
                        uuid
                )
        );
        return false;
    }

    private AuthResponse buildAuthResponse(User user) {
        return AuthResponse.builder()
                .userDTO(
                        UserDTO.builder()
                                .username(user.getUsername())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .role(user.getRole())
                                .build()
                )
                .accessToken(
                        tokenService.genToken(user)
                )
                .refreshToken(
                        refreshTokenService.createRefreshToken(user)
                )
                .verified(true)
                .build();
    }
}
