package com.project.server.service;


import com.project.server.model.entity.PasswordToken;
import com.project.server.model.entity.User;
import com.project.server.repository.PasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordTokenService {
    private final PasswordTokenRepository passwordTokenRepository;

    @Value("${app.token.passwordExpire}")
    private String expireTime;

    public String createPasswordToken(User user) {
        PasswordToken passwordToken = PasswordToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)))
                .build();
        return passwordTokenRepository.save(passwordToken).getToken();
    }

    public void deletePasswordToken(String token) {
        passwordTokenRepository.deleteByToken(token);
    }

    public PasswordToken getPasswordToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }
}
