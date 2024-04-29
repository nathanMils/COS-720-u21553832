package com.project.server.repository;

import com.project.server.model.entity.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    PasswordToken findByToken(String token);
    void deleteByToken(String token);
}
