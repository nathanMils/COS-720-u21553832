package com.project.server.repository;

import com.project.server.model.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module,Long> {

    Optional<Module> findByName(String name);
    boolean existsByName(String name);
}
