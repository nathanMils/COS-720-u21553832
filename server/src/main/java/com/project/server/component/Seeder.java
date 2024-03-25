package com.project.server.component;

import com.project.server.model.entity.Role;
import com.project.server.model.entity.RoleEnum;
import com.project.server.model.entity.User;
import com.project.server.repository.RoleRepository;
import com.project.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Seeder implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    boolean done = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (done) return;

        this.loadRoles();
        this.createAdministrator();

        done = true;
    }

    // This loads all roles we want before runtime
    @Transactional
    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] {
                RoleEnum.ROLE_APPLICANT,
                RoleEnum.ROLE_STUDENT,
                RoleEnum.ROLE_MODERATOR,
                RoleEnum.ROLE_ADMIN
        };

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName.name());

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = Role.builder()
                                        .name(roleName.name())
                                        .users(new ArrayList<>())
                                        .build();
                roleRepository.save(roleToCreate);
            });
        });
    }

    @Transactional
    private void createAdministrator() {

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN.name());
        Optional<User> optionalUser = userRepository.findByUsername("admin");

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }
        Role role = optionalRole.orElseThrow();
        User user = User.builder()
                .username("admin")
                .email("admin@email.com")
                .firstName("admin")
                .lastName("admin")
                .password(passwordEncoder.encode("admin"))
                .role(role)
                .build();
        userRepository.save(user);
    }
}