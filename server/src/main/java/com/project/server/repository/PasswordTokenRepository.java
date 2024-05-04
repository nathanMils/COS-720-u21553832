package com.project.server.repository;

import com.project.server.model.entity.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    PasswordToken findByToken(String token);
    Optional<PasswordToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    void deleteByToken(String token);
}
