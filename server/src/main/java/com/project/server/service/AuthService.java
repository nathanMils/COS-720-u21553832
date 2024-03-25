package com.project.server.service;

import com.project.server.exception.PasswordDoesNotMatchException;
import com.project.server.exception.RefreshTokenException;
import com.project.server.exception.UsernameAlreadyExists;
import com.project.server.model.entity.RefreshToken;
import com.project.server.model.entity.RoleEnum;
import com.project.server.model.entity.User;
import com.project.server.repository.RoleRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.RefreshRequest;
import com.project.server.response.auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse apply(
            ApplicationRequest request
    ) {
        if (userRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new UsernameAlreadyExists();
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(roleRepository.findByName(RoleEnum.ROLE_APPLICANT.name()).orElseThrow())
                .enabled(true)
                .build();
        userRepository.save(
                user
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
