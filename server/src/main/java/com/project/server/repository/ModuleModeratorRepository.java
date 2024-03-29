package com.project.server.repository;

import com.project.server.model.entity.ModuleModerator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleModeratorRepository extends JpaRepository<ModuleModerator,Long> {
    List<ModuleModerator> findByUserId(Long userId);
}
