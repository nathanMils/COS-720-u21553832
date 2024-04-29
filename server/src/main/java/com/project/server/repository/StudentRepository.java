package com.project.server.repository;

import com.project.server.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByUserId(Long userId);
    List<Student> findByUserIdAndCourseId(Long userId, UUID courseId);
    Optional<Student> findByUserIdAndModuleId(Long userId, UUID moduleId);
    void deleteAllByUserIdAndCourseId(Long userId, UUID courseId);
}
