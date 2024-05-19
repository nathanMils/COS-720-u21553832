package com.project.server.component;

import com.project.server.model.enums.RoleEnum;
import com.project.server.model.entity.User;
import com.project.server.repository.UserRepository;
import com.project.server.service.OTPService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.seed", havingValue = "true")
public class Seeder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.name.first}")
    private String adminFirstName;

    @Value("${app.admin.name.last}")
    private String adminLastName;

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
    public void createAdministrator() {
        userRepository.findByUsername(adminUsername).ifPresentOrElse(
                admin -> {
                    // Do nothing
                },
                () ->
                    userRepository.save(
                            User.builder()
                                    .username(adminUsername)
                                    .email(adminEmail)
                                    .firstName(adminFirstName)
                                    .lastName(adminLastName)
                                    .password(passwordEncoder.encode(adminPassword))
                                    .role(RoleEnum.ROLE_ADMIN)
                                    .secret(otpService.generateSecret())
                                    .enabled(true)
                                    .build()
                    )

        );
        userRepository.findByUsername("Course_Moderator_A").ifPresentOrElse(
                courseModerator -> {
                    // Do nothing
                },
                () ->
                        userRepository.save(
                                User.builder()
                                        .username("Course_Moderator_A")
                                        .email(adminEmail)
                                        .firstName("Course")
                                        .lastName("Moderator")
                                        .password(passwordEncoder.encode("password_courseA"))
                                        .role(RoleEnum.ROLE_COURSE_MODERATOR)
                                        .secret(otpService.generateSecret())
                                        .enabled(true)
                                        .build()
                        )
        );
        userRepository.findByUsername("Course_Moderator_B").ifPresentOrElse(
                courseModerator -> {
                    // Do nothing
                },
                () ->
                        userRepository.save(
                                User.builder()
                                        .username("Course_Moderator_B")
                                        .email(adminEmail)
                                        .firstName("Course")
                                        .lastName("Moderator")
                                        .password(passwordEncoder.encode("password_courseB"))
                                        .role(RoleEnum.ROLE_COURSE_MODERATOR)
                                        .secret(otpService.generateSecret())
                                        .enabled(true)
                                        .build()
                        )
        );
        userRepository.findByUsername("Course_Moderator_C").ifPresentOrElse(
                courseModerator -> {
                    // Do nothing
                },
                () ->
                        userRepository.save(
                                User.builder()
                                        .username("Course_Moderator_C")
                                        .email(adminEmail)
                                        .firstName("Course")
                                        .lastName("Moderator")
                                        .password(passwordEncoder.encode("password_courseC"))
                                        .role(RoleEnum.ROLE_COURSE_MODERATOR)
                                        .secret(otpService.generateSecret())
                                        .enabled(true)
                                        .build()
                        )
        );


    }
}