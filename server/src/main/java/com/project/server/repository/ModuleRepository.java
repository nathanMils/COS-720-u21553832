package com.project.server.repository;

import com.project.server.model.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<ModuleEntity,Long> {

    Optional<ModuleEntity> findByModuleName(String moduleName);
    boolean existsByModuleName(String moduleName);
}
