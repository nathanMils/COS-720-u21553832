package com.project.server.service;

import com.project.server.model.entity.Post;
import com.project.server.repository.ModuleRepository;
import com.project.server.repository.PostRepository;
import com.project.server.request.moduleModerator.AddPostRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModuleModeratorService {

    private final PostRepository postRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public void addPost(AddPostRequest request) {
        moduleRepository.findById(UUID.fromString(request.moduleId())).ifPresentOrElse(
                (module) -> {
                    postRepository.save(
                            Post.builder()
                                    .module(module)
                                    .content(request.content())
                                    .build()
                    );
                },
                () -> {
                    throw new EntityNotFoundException("MODULE_NOT_FOUND");
                }
        );
    }
}
