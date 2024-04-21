package com.project.server.service;

import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.StudentApplicationDTO;
import com.project.server.model.entity.Course;
import com.project.server.model.entity.StudentApplication;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.CourseRepository;
import com.project.server.repository.StudentApplicationRepository;
import com.project.server.request.admin.AcceptRequest;
import com.project.server.request.admin.CreateCourseRequest;
import com.project.server.request.admin.DenyRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final StudentApplicationRepository studentApplicationRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public List<StudentApplicationDTO> fetchStudentApplications() {
        return studentApplicationRepository.findByStatus(StatusEnum.PENDING)
                .stream()
                .map(StudentApplication::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseDTO createCourse(CreateCourseRequest request) {
        if (courseRepository.findByName(request.name()).isPresent()) throw new EntityExistsException("COURSE_NAME_EXISTS");
        return courseRepository.save(
                Course.builder()
                        .name(request.name())
                        .description(request.description())
                        .build()
            ).convert();
    }

    @Transactional
    public void deleteCourse(UUID courseId) {
        courseRepository.findById(courseId).ifPresentOrElse(
                courseRepository::delete,
                () -> {throw new EntityNotFoundException("COURSE_NOT_FOUND");}
        );
    }

    @Transactional
    public void acceptApplicant(AcceptRequest request) {
        studentApplicationRepository.findById(request.applicationId()).ifPresentOrElse(
                (application) -> {
                    application.setStatus(StatusEnum.ACCEPTED);
                    studentApplicationRepository.save(application);
                },
                () -> {throw new EntityNotFoundException("APPLICATION_NOT_FOUND");}
        );
    }

    @Transactional
    public void rejectApplicant(DenyRequest request) {
        studentApplicationRepository.findById(request.applicationId()).ifPresentOrElse(
                (application) -> {
                    application.setStatus(StatusEnum.DENIED);
                    studentApplicationRepository.save(application);
                },
                () -> {throw new EntityNotFoundException("APPLICATION_NOT_FOUND");}
        );
    }
}
