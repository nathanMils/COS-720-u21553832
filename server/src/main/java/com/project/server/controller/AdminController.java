package com.project.server.controller;

import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.StudentApplicationDTO;
import com.project.server.request.admin.AcceptRequest;
import com.project.server.request.admin.CreateModeratorRequest;
import com.project.server.request.admin.DenyRequest;
import com.project.server.response.APIResponse;
import com.project.server.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;

    @GetMapping("/fetchApplications")
    public ResponseEntity<APIResponse<List<StudentApplicationDTO>>> fetchStudentApplications() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(adminService.fetchStudentApplications())
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
                        APIResponse.success()
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
                        APIResponse.success()
                );
    }

    @GetMapping("/fetchCourses")
    public ResponseEntity<APIResponse<List<CourseDTO>>> fetchCourses() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(adminService.fetchCourses())
                );
    }

    @PostMapping("/createModerator")
    public ResponseEntity<APIResponse<Void>> createModerator(
            @RequestBody @Valid CreateModeratorRequest request
    ) {
        adminService.createModerator(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }
}
