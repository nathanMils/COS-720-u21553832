package com.project.server.repository;

import com.project.server.model.entity.User;
import com.project.server.model.projections.auth.UserAuthProjection;
import com.project.server.model.projections.authorizationProjections.AuthUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<AuthUserProjection> findAuthProjectedByUsername(String username);
    Optional<UserAuthProjection> findUserProjectedByUsername(String username);
    Optional<UserAuthProjection> findUserProjectedById(Long id);
    boolean existsByUsername(String username);
}
