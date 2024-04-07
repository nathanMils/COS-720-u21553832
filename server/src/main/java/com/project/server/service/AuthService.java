package com.project.server.service;

import com.project.server.exception.PasswordDoesNotMatchException;
import com.project.server.exception.RefreshTokenException;
import com.project.server.model.entity.*;
import com.project.server.model.enums.RoleEnum;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.StudentApplicationRepository;
import com.project.server.repository.CourseRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.RefreshRequest;
import com.project.server.response.auth.AuthResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final StudentApplicationRepository studentApplicationRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Transactional
    public AuthResponse apply(
            ApplicationRequest request
    ) {
        userRepository
                .findByUsername(request.getUsername())
                .ifPresent(
                        (user) -> {
                            throw new EntityExistsException("USERNAME_EXISTS");
                        }
                );
        User user = userRepository.save(
                User.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .role(RoleEnum.ROLE_STUDENT)
                        .enabled(true)
                        .build()
        );
        studentApplicationRepository.save(
                StudentApplication.builder()
                        .user(
                                user
                        )
                        .course(
                                courseRepository.findById(UUID.fromString(request.getCourseId()))
                                        .orElseThrow(
                                                () -> new EntityNotFoundException("COURSE_NOT_FOUND")
                                        )
                        )
                        .status(StatusEnum.PENDING)
                        .build()
        );
        return AuthResponse.builder()
                .accessToken(
                        tokenService.genToken(
                                user
                        )
                )
                .refreshToken(
                        refreshTokenService.createRefreshToken(
                                user
                        )
                )
                .build();
    }

    @Transactional
    public AuthResponse login (
            LoginRequest request
    ) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .accessToken(
                            tokenService.genToken(
                                    user
                            )
                    )
                    .refreshToken(
                            refreshTokenService.createRefreshToken(
                                    user
                            )
                    )
                    .build();
        } else {
            throw new PasswordDoesNotMatchException();
        }
    }

    @Transactional
    public AuthResponse refresh(
        RefreshRequest request
    ) {
        RefreshToken token = refreshTokenService.verifyExpiration(request.getToken());
        if (token.isRevoked()) {
            throw new RefreshTokenException("TOKEN_REVOKED");
        }
        return AuthResponse.builder()
                .accessToken(
                        tokenService.genToken(
                                token.getUser()
                        )
                )
                .refreshToken(
                        refreshTokenService.createRefreshToken(
                                token.getUser()
                        )
                )
                .build();
    }
}
