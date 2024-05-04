package com.project.server.service;

import com.project.server.exception.ConfirmationTokenException;
import com.project.server.model.entity.ConfirmationToken;
import com.project.server.model.entity.User;
import com.project.server.model.projections.auth.UserAuthProjection;
import com.project.server.repository.ConfirmationTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Value("${app.token.confirmationExpire}")
    private String expireTime;

    public String generateConfirmationToken(UserAuthProjection user) {
        Optional<ConfirmationToken> existingTokenOpt = confirmationTokenRepository.findByUserId(user.getId());
        if (existingTokenOpt.isPresent()) {
            ConfirmationToken existingToken = existingTokenOpt.get();
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setExpiryDate(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)));
            return confirmationTokenRepository.save(existingToken).getToken();
        } else {
            ConfirmationToken confirmationToken = ConfirmationToken.builder()
                    .userId(user.getId())
                    .token(UUID.randomUUID().toString())
                    .expiryDate(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)))
                    .build();
            return confirmationTokenRepository.save(confirmationToken).getToken();
        }
    }

    public String generateConfirmationToken(User user) {
        Optional<ConfirmationToken> existingTokenOpt = confirmationTokenRepository.findByUserId(user.getId());
        if (existingTokenOpt.isPresent()) {
            ConfirmationToken existingToken = existingTokenOpt.get();
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setExpiryDate(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)));
            return confirmationTokenRepository.save(existingToken).getToken();
        } else {
            ConfirmationToken confirmationToken = ConfirmationToken.builder()
                    .userId(user.getId())
                    .token(UUID.randomUUID().toString())
                    .expiryDate(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)))
                    .build();
            return confirmationTokenRepository.save(confirmationToken).getToken();
        }
    }

    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository.findByToken(token).orElse(null);
    }
}
