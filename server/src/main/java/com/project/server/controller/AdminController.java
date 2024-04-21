package com.project.server.controller;

import com.project.server.constraint.ValidUUID;
import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.StudentApplicationDTO;
import com.project.server.request.admin.AcceptRequest;
import com.project.server.request.admin.CreateCourseRequest;
import com.project.server.request.admin.DenyRequest;
import com.project.server.response.APIResponse;
import com.project.server.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;

    @GetMapping("/fetchApplications")
    public ResponseEntity<APIResponse<List<StudentApplicationDTO>>> fetchStudentApplications() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                adminService.fetchStudentApplications(),
                                "SUCCESS"
                        )
                );
    }

    @PostMapping("/acceptStudent")
    public ResponseEntity<APIResponse<Void>> acceptStudent(
            @RequestBody @Valid AcceptRequest request
    ) {
        adminService.acceptApplicant(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }

    @PostMapping("/denyStudent")
    public ResponseEntity<APIResponse<Void>> denyStudent(
            @RequestBody @Valid DenyRequest request
    ) {
        adminService.rejectApplicant(request);
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
            @RequestBody @Valid CreateCourseRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                adminService.createCourse(request),
                                "SUCCESS"
                        )
                );
    }

    @DeleteMapping("/deleteCourse/{courseId}")
    public ResponseEntity<APIResponse<Void>> deleteCourse(
            @ValidUUID @PathVariable String courseId
    ) {
        adminService.deleteCourse(UUID.fromString(courseId));
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
