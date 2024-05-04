package com.project.server.repository;

import com.project.server.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post,UUID> {
    List<Post> findByModuleId(UUID moduleId);
}
