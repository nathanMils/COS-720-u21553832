package com.project.server.controller;

import com.project.server.constraint.ValidUUID;
import com.project.server.response.APIResponse;
import com.project.server.response.student.FetchStudentApplicationsResponse;
import com.project.server.response.open.FetchCoursesResponse;
import com.project.server.response.module.FetchModulesResponse;
import com.project.server.response.student.FetchModuleContentResponse;
import com.project.server.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/fetchApplications")
    public ResponseEntity<APIResponse<FetchStudentApplicationsResponse>> fetchApplications() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                studentService.fetchStudentApplications(),
                                "SUCCESS"
                        )
                );
    }
    @PostMapping("/apply/{courseId}")
    public ResponseEntity<APIResponse<Void>> apply(
            @ValidUUID @PathVariable String courseId
    ) {
        studentService.apply(UUID.fromString(courseId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }

    @PostMapping("/drop/{courseId}")
    public ResponseEntity<APIResponse<Void>> drop(
            @ValidUUID @PathVariable String courseId
    ) {
        studentService.drop(UUID.fromString(courseId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }

    @GetMapping("/fetchCourses")
    public ResponseEntity<APIResponse<FetchCoursesResponse>> getCourses() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                            studentService.fetchStudentCourses(),
                            "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_student')")
    @GetMapping("/fetchModules/{courseId}")
    public ResponseEntity<APIResponse<FetchModulesResponse>> fetchCourseModules(
            @ValidUUID @PathVariable String courseId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                studentService.fetchCourseModules(UUID.fromString(courseId)),
                                "SUCCESS"
                        )
                );
    }

    @GetMapping("/fetchModules")
    public ResponseEntity<APIResponse<FetchModulesResponse>> fetchModules() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                studentService.fetchStudentModules(),
                                "SUCCESS"
                        )
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_student') || hasRole('ADMIN')")
    @PostMapping("/register/{courseId}/{moduleId}")
    public ResponseEntity<APIResponse<Void>> register(
            @ValidUUID @PathVariable String courseId,
            @ValidUUID @PathVariable String moduleId
    ) {
        studentService.registerStudent(UUID.fromString(courseId),UUID.fromString(moduleId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(null,"SUCCESS")
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_module_' + #moduleId + '_student') || hasRole('ADMIN')")
    @PostMapping("/deRegister/{courseId}/{moduleId}")
    public ResponseEntity<APIResponse<Void>> deRegister(
            @ValidUUID @PathVariable String courseId,
            @ValidUUID @PathVariable String moduleId
    ) {
        System.out.println("Here");
        studentService.deRegisterStudent(UUID.fromString(moduleId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(null,"SUCCESS")
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_module_' + #moduleId + '_student')")
    @GetMapping("/fetchContent/{courseId}/{moduleId}")
    public ResponseEntity<APIResponse<FetchModuleContentResponse>> fetchModuleContent(
            @ValidUUID @PathVariable String courseId,
            @ValidUUID @PathVariable String moduleId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                studentService.fetchModuleContent(UUID.fromString(moduleId)),
                                "SUCCESS"
                        )
                );
    }
}
