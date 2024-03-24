package com.project.server.service;

import com.project.server.model.entity.RoleEnum;
import com.project.server.model.entity.User;
import com.project.server.repository.RoleRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.response.ResponseCode;
import com.project.server.response.auth.LoginResponse;
import com.project.server.response.auth.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public ApplicationResponse apply(
            ApplicationRequest request
    ) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ApplicationResponse.builder()
                    .code(ResponseCode.failed)
                    .build();
        }
        User user = User.builder()
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
        return ApplicationResponse.builder()
                .code(ResponseCode.success)
                .token(
                        tokenService.genToken(
                                user
                        )
                )
                .build();
    }

    public LoginResponse login (
            LoginRequest request
    ) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException(request.getEmail()));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponse.builder()
                    .token(
                            tokenService.genToken(
                                    user
                            )
                    )
                    .code(ResponseCode.success)
                    .build();
        } else {
            return LoginResponse.builder()
                    .code(ResponseCode.failed)
                    .build();
        }
    }
}
