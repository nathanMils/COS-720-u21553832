package com.project.server.repository;

import com.project.server.model.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    Optional<ConfirmationToken> findByToken(String token);

    Optional<ConfirmationToken> findByUserId(Long userId);
}
