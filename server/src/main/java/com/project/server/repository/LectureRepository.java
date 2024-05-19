package com.project.server.repository;

import com.project.server.model.entity.Lecture;
import com.project.server.model.projections.LectureProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LectureRepository extends JpaRepository<Lecture, UUID> {
    List<LectureProjection> findAllByModuleId(UUID moduleId);
    boolean existsByFileName(String name);
}
