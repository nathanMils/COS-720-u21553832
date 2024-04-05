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

    @PreAuthorize("hasAuthority('course_' + #request.getCourseId() + '_moderator') || hasRole('ADMIN')")
    @PostMapping("/createModule")
    public ResponseEntity<APIResponse<CreateModuleResponse>> createModule(
            @Valid @RequestBody CreateModuleRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                courseModeratorService.createModule(request),
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_moderator') || hasRole('ADMIN')")
    @PostMapping("/addModuleToCourse/{courseId}/{moduleId}")
    public ResponseEntity<APIResponse<Void>> addModuleToCourse(
            @ValidUUID @PathVariable String courseId,
            @ValidUUID @PathVariable String moduleId
    ) {
        courseModeratorService.addModuleToCourse(UUID.fromString(courseId),UUID.fromString(moduleId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('course_' + #request.getCourseId() + '_moderator') || hasRole('ADMIN')")
    @PostMapping("/removeModule")
    public ResponseEntity<APIResponse<Void>> removeModuleFromCourse(
            RemoveModuleFromCourseRequest request
    ) {
        Map<String,String> warnings = courseModeratorService.removeModuleFromCourse(request);
        if (request.isForce() || warnings.isEmpty()) {
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
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.<Void>builder()
                                    .status(ResponseCode.warning)
                                    .internalCode("WARNING")
                                    .warnings(warnings)
                                    .build()
                    );
        }
    }

    @PreAuthorize("hasAuthority('course_' + #coursId + '_moderator') || hasRole('ADMIN')")
    @PostMapping("/deleteModule/{courseId}/{moduleId}")
    public ResponseEntity<APIResponse<Void>> deleteModule(
            @ValidUUID @PathVariable String courseId,
            @ValidUUID @PathVariable String moduleId
    ) {
        courseModeratorService.destroyModule(UUID.fromString(courseId), UUID.fromString(moduleId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }
}
