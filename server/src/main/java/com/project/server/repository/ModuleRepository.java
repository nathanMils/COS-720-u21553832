package com.project.server.repository;

import com.project.server.model.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<ModuleEntity,Long> {
    boolean existsByModuleName(String moduleName);
}
