package com.project.server.service;

import com.project.server.model.entity.UserEntity;
import com.project.server.repository.ModuleRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.user.ModuleRegisterRequest;
import com.project.server.response.ResponseCode;
import com.project.server.response.user.ModuleRegisterResponse;
import lombok.RequiredArgsConstructor;
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
                .code(ResponseCode.success)
                .build();
    }

    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElseThrow();
    }
}
