package com.project.server.service;

import com.project.server.model.entity.UserEntity;
import com.project.server.repository.ModuleRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.user.ModuleRegisterRequest;
import com.project.server.response.Code;
import com.project.server.response.user.ModuleRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private ModuleRepository moduleRepository;

    public ModuleRegisterResponse register(
            ModuleRegisterRequest request
    ) {
        UserEntity user = getAuthenticatedUser();
        return ModuleRegisterResponse.builder()
                .code(Code.success)
                .build();
    }

    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return userRepository.findByUsername(authentication.getName()).orElseThrow();
        }
    }
}
