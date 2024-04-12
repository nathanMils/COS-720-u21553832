package com.project.server.component;

import com.project.server.model.enums.RoleEnum;
import com.project.server.model.entity.User;
import com.project.server.repository.UserRepository;
import com.project.server.service.OTPService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Seeder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OTPService otpService;

    boolean done = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (done) return;
        this.createAdministrator();
        done = true;
    }

    // This loads all roles we want before runtime


    @Transactional
    private void createAdministrator() {
        userRepository.findByUsername("admin").ifPresentOrElse(
                (admin) -> {},
                () -> {
                    userRepository.save(
                            User.builder()
                                    .username("admin")
                                    .email("admin@email.com")
                                    .firstName("admin")
                                    .lastName("admin")
                                    .password(passwordEncoder.encode("admin"))
                                    .role(RoleEnum.ROLE_ADMIN)
                                    .secret(otpService.generateSecret())
                                    .enabled(true)
                                    .build()
                    );
                }
        );
    }
}