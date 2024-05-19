package com.project.server.service;

import com.project.server.model.dto.LectureDTO;
import com.project.server.model.dto.PostDTO;
import com.project.server.model.entity.Lecture;
import com.project.server.model.entity.Post;
import com.project.server.repository.LectureRepository;
import com.project.server.repository.ModuleRepository;
import com.project.server.repository.PostRepository;
import com.project.server.request.moderator.AddPostRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModuleModeratorService {

    private final PostRepository postRepository;
    private final ModuleRepository moduleRepository;
    private final LectureRepository lectureRepository;

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

    @Transactional
    public LectureDTO uploadLecture(MultipartFile file, UUID moduleId) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (lectureRepository.existsByFileName(fileName))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"File with name " + fileName + " already exists");
        try {
            if(fileName.contains("..")) {
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Filename contains invalid path sequence " + fileName);
            }
            if (file.getBytes().length > (1024 * 1024)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"File size exceeds maximum limit");
            }
            Lecture lecture = Lecture.builder()
                    .fileName(fileName)
                    .module(
                            moduleRepository.findById(moduleId)
                                    .orElseThrow(() -> new EntityNotFoundException("MODULE_NOT_FOUND"))
                    )
                    .fileType(file.getContentType())
                    .content(file.getBytes())
                    .build();
            return lectureRepository.save(lecture).convert();
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(file.getSize());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to upload file " + fileName);
        }
    }

    @Transactional
    public void deleteLecture(UUID lectureId) {
        lectureRepository.deleteById(lectureId);
    }
}
