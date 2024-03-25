package com.project.server.service;

import com.project.server.exception.RefreshTokenException;
import com.project.server.model.entity.RefreshToken;
import com.project.server.model.entity.User;
import com.project.server.repository.RefreshTokenRepository;
import com.project.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${app.token.refresh}")
    private String expireTime;

    public String createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found")))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(Long.parseLong(expireTime)))
                .build();
        return refreshTokenRepository.save(refreshToken).getToken();
    }

    @Transactional
    public String createRefreshToken(User user) {
        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUser(user);
        if (existingTokenOpt.isPresent()) {
            RefreshToken existingToken = existingTokenOpt.get();
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setExpiryDate(Instant.now().plusMillis(Long.parseLong(expireTime)));
            return refreshTokenRepository.save(existingToken).getToken();
        } else {
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(Long.parseLong(expireTime)))
                    .build();
            return refreshTokenRepository.save(refreshToken).getToken();
        }
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken()," Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

    public RefreshToken verifyExpiration(String token) {
        return findByToken(token).orElseThrow(() -> new RefreshTokenException(token,"Refresh token not found"));
    }
}
