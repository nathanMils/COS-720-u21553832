package com.project.server.repository;

import com.project.server.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    Optional<Course> findByName(String name);
    List<Course> findAllByModeratorId(Long moderatorId);
}
