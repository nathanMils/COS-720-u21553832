package com.project.server.repository;

import com.project.server.model.entity.CourseModerator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseModeratorRepository extends JpaRepository<CourseModerator,Long> {
    List<CourseModerator> findByUserId(Long userId);
}