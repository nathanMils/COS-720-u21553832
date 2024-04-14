package com.project.server.controller;

import com.project.server.constraint.ValidUUID;
import com.project.server.request.courseModerator.CreateModuleRequest;
import com.project.server.request.courseModerator.RemoveModuleFromCourseRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import com.project.server.response.courseModerator.CreateModuleResponse;
import com.project.server.response.module.FetchModulesResponse;
import com.project.server.service.CourseModeratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/api/v1/courseModerator")
@RequiredArgsConstructor
public class CourseModeratorController {

    private final CourseModeratorService courseModeratorService;

    @PreAuthorize("hasAuthority('course_' + #courseId + '_moderator') || hasRole('ADMIN')")
    @GetMapping("/fetchModules/{courseId}")
    public ResponseEntity<APIResponse<FetchModulesResponse>> fetchModulesInCourse(
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

    @PreAuthorize("hasAuthority('course_' + #courseId + '_moderator') || hasRole('ADMIN')")
    @PostMapping("/{courseId}/createModule")
    public ResponseEntity<APIResponse<CreateModuleResponse>> createModule(
            @ValidUUID @PathVariable String courseId,
            @Valid @RequestBody CreateModuleRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                courseModeratorService.createModule(UUID.fromString(courseId),request),
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
}
