package com.project.server.service;


import com.project.server.model.entity.PasswordToken;
import com.project.server.model.entity.User;
import com.project.server.model.projections.auth.UserAuthProjection;
import com.project.server.repository.PasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordTokenService {
    private final PasswordTokenRepository passwordTokenRepository;

    @Value("${app.token.passwordExpire}")
    private String expireTime;

    public String createPasswordToken(UserAuthProjection user) {
        Optional<PasswordToken> existingToken = passwordTokenRepository.findByUserId(user.getId());
        PasswordToken passwordToken;
        if (existingToken.isPresent()) {
            passwordToken = existingToken.get();
            passwordToken.setToken(UUID.randomUUID().toString());
            passwordToken.setExpiryDate(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)));
        } else {
            passwordToken = PasswordToken.builder()
                    .userId(user.getId())
                    .token(UUID.randomUUID().toString())
                    .expiryDate(new Date(System.currentTimeMillis() + (Long.parseLong(expireTime) * 1000)))
                    .build();
        }
        return passwordTokenRepository.save(passwordToken).getToken();
    }

    public void deletePasswordToken(String token) {
        passwordTokenRepository.deleteByToken(token);
    }

    public PasswordToken getPasswordToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }
}
