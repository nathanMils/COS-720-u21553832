package com.project.server.service;

import com.project.server.exception.RefreshTokenException;
import com.project.server.model.entity.RefreshToken;
import com.project.server.model.projections.auth.UserAuthProjection;
import com.project.server.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    
    @Value("${app.token.refreshExpire}")
    private String expireTime;

    public String createRefreshToken(UserAuthProjection user) {
        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUserId(user.getId());
        if (existingTokenOpt.isPresent()) {
            RefreshToken existingToken = existingTokenOpt.get();
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setExpiryDate(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)));
            existingToken.setRevoked(false);
            return refreshTokenRepository.save(existingToken).getToken();
        } else {
            RefreshToken refreshToken = RefreshToken.builder()
                    .userId(user.getId())
                    .token(UUID.randomUUID().toString())
                    .expiryDate(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)))
                    .revoked(false)
                    .build();
            return refreshTokenRepository.save(refreshToken).getToken();
        }
    }

    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(
            rToken -> {
                rToken.setRevoked(true);
                refreshTokenRepository.save(rToken);
            }
        );
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException("REFRESH_TOKEN_NOT_FOUND"));
    }
}
