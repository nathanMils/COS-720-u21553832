package com.project.server.repository;

import com.project.server.model.entity.StudentApplication;
import com.project.server.model.enums.StatusEnum;
import com.project.server.model.projections.authorization.AuthApplicationProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentApplicationRepository extends JpaRepository<StudentApplication, Long> {
    Optional<StudentApplication> findByUserIdAndCourseIdAndStatus(Long userId, UUID course, StatusEnum status);
    Optional<StudentApplication> findByUserIdAndCourseId(Long userId, UUID courseId);
    void deleteByUserIdAndCourseId(Long userId, UUID course);
    List<StudentApplication> findByUserId(Long userId);
    List<StudentApplication> findByUserIdAndStatus(Long userId, StatusEnum status);
    List<StudentApplication> findByStatus(StatusEnum status);

    // Authorizations
    List<AuthApplicationProjection> findAuthProjectionByUserIdAndStatus(Long userId, StatusEnum status);
}
