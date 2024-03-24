package com.project.server.service;

import com.project.server.model.entity.Role;
import com.project.server.model.entity.RoleEnum;
import com.project.server.model.entity.User;
import com.project.server.repository.RoleRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.user.UpgradeUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void upgradeUser(UpgradeUserRequest request) {
        User user = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUserEmail()));

        Role studentRole = roleRepository.findByName(RoleEnum.ROLE_STUDENT.name())
                .orElseThrow(() -> new RuntimeException("Role STUDENT not found"));

        user.setRole(studentRole);
        userRepository.save(user);
    }
}
