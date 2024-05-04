package com.project.server.service;

import com.project.server.model.dto.PostDTO;
import com.project.server.model.entity.Post;
import com.project.server.repository.ModuleRepository;
import com.project.server.repository.PostRepository;
import com.project.server.request.moduleModerator.AddPostRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModuleModeratorService {

    private final PostRepository postRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public PostDTO addPost(AddPostRequest request, UUID moduleId) {
        PostDTO postDTO = postRepository.save(
                Post.builder()
                        .content(request.content())
                        .module(
                                moduleRepository.findById(moduleId)
                                        .orElseThrow(() -> new EntityNotFoundException("MODULE_NOT_FOUND"))
                        )
                        .build()
        ).convert();
        postDTO.setCreatedAt(Date.from(Instant.now()));
        return postDTO;
    }

    @Transactional
    public void deletePost(UUID postId) {
        postRepository.deleteById(postId);
    }
}
