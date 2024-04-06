package com.project.server.controller;

import com.project.server.request.admin.AcceptRequest;
import com.project.server.request.admin.CreateCourseRequest;
import com.project.server.request.admin.DestroyCourseRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import com.project.server.response.admin.CreateCourseResponse;
import com.project.server.response.admin.FetchStudentApplicationsResponse;
import com.project.server.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;

    @GetMapping("/fetchStudentApplicants")
    public ResponseEntity<APIResponse<FetchStudentApplicationsResponse>> fetchStudentApplications() {
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
                                "USER_STUDENT"
                        )
                );
    }

    @PostMapping("/acceptCourseModerator")
    public ResponseEntity<APIResponse<Void>> acceptModerator(
            @RequestBody AcceptRequest request
    ) {
        adminService.acceptCourseModerator(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "USER_MODERATOR"
                        )
                );
    }

    @PostMapping("/createCourse")
    public ResponseEntity<APIResponse<CreateCourseResponse>> createCourse(
            @RequestBody @Valid CreateCourseRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                adminService.createCourse(request),
                                "COURSE_CREATED"
                        )
                );
    }

    @PostMapping("/destroyCourse")
    public ResponseEntity<APIResponse<Void>> destroyCourse(
            @RequestBody @Valid DestroyCourseRequest request
    ) {
        Map<String, String> warnings = adminService.destroyCourse(request);
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
}
