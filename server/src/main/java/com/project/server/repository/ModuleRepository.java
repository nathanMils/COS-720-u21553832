package com.project.server.repository;

import com.project.server.model.entity.Module;
import com.project.server.model.projections.ModuleProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID> {
    Optional<Module> findByName(String name);
    List<Module> findByCourseId(UUID courseId);
    Optional<ModuleProjection> findModuleProjectionById(UUID id);
    boolean existsByName(String name);
}
