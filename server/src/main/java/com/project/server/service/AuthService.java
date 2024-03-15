package com.project.server.service;

import com.project.server.model.entity.Role;
import com.project.server.model.entity.UserEntity;
import com.project.server.repository.UserRepository;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.RegistrationRequest;
import com.project.server.response.Code;
import com.project.server.response.auth.LoginResponse;
import com.project.server.response.auth.RegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public RegistrationResponse registerStudent(
            RegistrationRequest request
    ) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return RegistrationResponse.builder()
                    .code(Code.failed)
                    .build();
        }
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .enabled(true)
                .build();
        userRepository.save(
                user
        );
        return RegistrationResponse.builder()
                .code(Code.success)
                .token(
                        tokenService.genToken(user)
                )
                .build();
    }

    public LoginResponse login (
            LoginRequest request
    ) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            UserEntity user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return LoginResponse.builder()
                        .token(
                                tokenService.genToken(
                                        user
                                )
                        )
                        .code(Code.success)
                        .build();
            } else {
                return LoginResponse.builder()
                        .code(Code.incorrectPassword)
                        .build();
            }

        } else {
            return LoginResponse.builder()
                    .code(Code.failed)
                    .build();
        }
    }
}
