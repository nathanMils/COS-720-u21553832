package com.project.server.controller;

import com.project.server.constraint.Sanitize;
import com.project.server.constraint.ValidUUID;
import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.ModuleDTO;
import com.project.server.model.dto.PostDTO;
import com.project.server.request.admin.CreateCourseRequest;
import com.project.server.request.courseModerator.CreateModuleRequest;
import com.project.server.request.moduleModerator.AddPostRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.courseModerator.FetchCourseResponse;
import com.project.server.response.student.FetchModuleContentResponse;
import com.project.server.service.CourseModeratorService;
import com.project.server.service.ModuleModeratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/api/v1/courseModerator")
@RequiredArgsConstructor
public class CourseModeratorController {

    private final CourseModeratorService courseModeratorService;
    private final ModuleModeratorService moduleModeratorService;

    @PreAuthorize("hasAuthority('course_' + #courseId + '_moderator') || hasRole('ADMIN')")
    @GetMapping("/fetch/{courseId}")
    public ResponseEntity<APIResponse<FetchCourseResponse>> fetchCourse(
            @ValidUUID @PathVariable String courseId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                courseModeratorService.fetchCourse(UUID.fromString(courseId)),
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_moderator') || hasRole('ADMIN')")
    @GetMapping("/fetchModules/{courseId}")
    public ResponseEntity<APIResponse<List<ModuleDTO>>> fetchModulesInCourse(
            @ValidUUID @PathVariable String courseId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                courseModeratorService.fetchModulesInCourse(UUID.fromString(courseId)),
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('module_' + #moduleId + '_moderator') || hasRole('ADMIN')")
    @PostMapping("/post/{moduleId}")
    public ResponseEntity<APIResponse<PostDTO>> addPost(
            @ValidUUID @PathVariable String moduleId,
            @Valid @RequestBody @Sanitize AddPostRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                moduleModeratorService.addPost(request,UUID.fromString(moduleId)),
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('module_' + #moduleId + '_moderator') || hasRole('ADMIN')")
    @DeleteMapping("/deletePost/{moduleId}/{postId}")
    public ResponseEntity<APIResponse<Void>> deletePost(
            @ValidUUID @PathVariable String moduleId,
            @ValidUUID @PathVariable String postId
    ) {
        moduleModeratorService.deletePost(UUID.fromString(postId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_moderator') || hasRole('ADMIN')")
    @PostMapping("/createModule/{courseId}")
    public ResponseEntity<APIResponse<Void>> createModule(
            @ValidUUID @PathVariable String courseId,
            @Valid @RequestBody @Sanitize CreateModuleRequest request
    ) {
        courseModeratorService.createModule(UUID.fromString(courseId), request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_moderator') || hasRole('ADMIN')")
    @DeleteMapping("/deleteModule/{courseId}/{moduleId}")
    public ResponseEntity<APIResponse<Void>> deleteModule(
            @ValidUUID @PathVariable String courseId,
            @ValidUUID @PathVariable String moduleId
    ) {
        if (courseModeratorService.deleteModule(UUID.fromString(courseId), UUID.fromString(moduleId))) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success(
                                    null,
                                    "SUCCESS"
                            )
                    );
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            APIResponse.error("MODULE_NOT_IN_COURSE")
                    );
        }

    }

    @PreAuthorize("hasRole('ADMIN') || hasAuthority('course_' + #courseId + '_moderator')")
    @DeleteMapping("/deleteCourse/{courseId}")
    public ResponseEntity<APIResponse<Void>> deleteCourse(
            @ValidUUID @PathVariable String courseId
    ) {
        courseModeratorService.deleteCourse(UUID.fromString(courseId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }

    @PostMapping("/createCourse")
    public ResponseEntity<APIResponse<CourseDTO>> createCourse(
            @RequestBody @Valid @Sanitize CreateCourseRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                courseModeratorService.createCourse(request),
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('module_' + #moduleId + '_moderator') || hasRole('ADMIN')")
    @GetMapping("/fetchModule/{moduleId}")
    public ResponseEntity<APIResponse<FetchModuleContentResponse>> fetchModule(
            @ValidUUID @PathVariable String moduleId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                courseModeratorService.fetchModule(UUID.fromString(moduleId)),
                                "SUCCESS"
                        )
                );
    }
}
