package com.project.server.service.emailservice.impl;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {


    @Async
    void sendDeviceVerificationEmail(String userEmail, String code);

    @Async
    void sendModeratorEmail(
            String userEmail,
            String username,
            String password,
            String firstName,
            String lastName
    );

    @Async
    void sendEmailVerificationEmail(String userEmail, String code);

    @Async
    void sendUserAlert(String userEmail, String userName);

    @Async
    void sendPasswordResetEmail(String userEmail, String token);
}
