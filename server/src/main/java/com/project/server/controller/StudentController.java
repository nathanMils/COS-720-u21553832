package com.project.server.controller;

import com.project.server.constraint.ValidUUID;
import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.ModuleDTO;
import com.project.server.model.dto.StudentApplicationDTO;
import com.project.server.model.entity.Lecture;
import com.project.server.response.APIResponse;
import com.project.server.response.student.FetchLectureResponse;
import com.project.server.response.student.FetchModuleContentResponse;
import com.project.server.response.student.FetchStudentCourseResponse;
import com.project.server.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PreAuthorize("hasRole('ADMIN') || hasAuthority('course_' + #courseId + '_student')")
    @GetMapping("/fetch/{courseId}")
    public ResponseEntity<APIResponse<FetchStudentCourseResponse>> fetchCourse(
            @ValidUUID @PathVariable String courseId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(studentService.fetchCourse(UUID.fromString(courseId)))
                );
    }

    @GetMapping("/fetchApplications")
    public ResponseEntity<APIResponse<List<StudentApplicationDTO>>> fetchApplications() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(studentService.fetchStudentApplications())
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
                        APIResponse.success()
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
                        APIResponse.success()
                );
    }

    @PostMapping("/dropApplication/{applicationId}")
    public ResponseEntity<APIResponse<Void>> dropApplication(
            @PathVariable String applicationId
    ) {
        studentService.dropApplication(Long.parseLong(applicationId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }

    @GetMapping("/fetchCourses")
    public ResponseEntity<APIResponse<List<CourseDTO>>> getCourses() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(studentService.fetchStudentCourses())
                );
    }

    @GetMapping("/fetchOtherCourses")
    public ResponseEntity<APIResponse<List<CourseDTO>>> fetchCourses() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(studentService.fetchOtherCourses())
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_student')")
    @GetMapping("/fetchModules/{courseId}")
    public ResponseEntity<APIResponse<List<ModuleDTO>>> fetchCourseModules(
            @ValidUUID @PathVariable String courseId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(studentService.fetchCourseModules(UUID.fromString(courseId)))
                );
    }

    @GetMapping("/fetchModules")
    public ResponseEntity<APIResponse<List<ModuleDTO>>> fetchModules() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(studentService.fetchStudentModules())
                );
    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_student') || hasRole('ADMIN')")
    @PostMapping("/register/{courseId}/{moduleId}")
    public ResponseEntity<APIResponse<Void>> register(
            @ValidUUID @PathVariable String courseId,
            @ValidUUID @PathVariable String moduleId
    ) {
        if(studentService.registerStudent(UUID.fromString(courseId),UUID.fromString(moduleId))) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success()
                    );
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        APIResponse.error("MODULE_NOT_IN_COURSE")
                );

    }

    @PreAuthorize("hasAuthority('course_' + #courseId + '_module_' + #moduleId + '_student') || hasRole('ADMIN')")
    @PostMapping("/deRegister/{courseId}/{moduleId}")
    public ResponseEntity<APIResponse<Void>> deRegister(
            @ValidUUID @PathVariable String courseId,
            @ValidUUID @PathVariable String moduleId
    ) {
        studentService.deRegisterStudent(UUID.fromString(moduleId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }

    @PreAuthorize("hasAuthority('module_' + #moduleId + '_student') || hasRole('ADMIN')")
    @GetMapping("/fetchContent/{moduleId}")
    public ResponseEntity<APIResponse<FetchModuleContentResponse>> fetchModuleContent(
            @ValidUUID @PathVariable String moduleId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(studentService.fetchModuleContent(UUID.fromString(moduleId)))
                );
    }

    @PreAuthorize("hasAuthority('lecture_' + #lectureId + '_student') || hasAuthority('lecture_'+ #lectureId + '_moderator') || hasRole('ADMIN')")
    @GetMapping("/fetchLecture/{lectureId}")
    public ResponseEntity<Resource> fetchLecture(
            @ValidUUID @PathVariable String lectureId,
            HttpServletResponse response
    ) {
        Lecture lecture = studentService.fetchLecture(UUID.fromString(lectureId));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + lecture.getFileName() + "\"");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ByteArrayResource(lecture.getContent())
                );
    }
}
