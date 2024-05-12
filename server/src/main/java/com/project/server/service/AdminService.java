package com.project.server.service;

import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.StudentApplicationDTO;
import com.project.server.model.entity.Course;
import com.project.server.model.entity.StudentApplication;
import com.project.server.model.entity.User;
import com.project.server.model.enums.RoleEnum;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.CourseRepository;
import com.project.server.repository.StudentApplicationRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.admin.AcceptRequest;
import com.project.server.request.admin.CreateModeratorRequest;
import com.project.server.request.admin.DenyRequest;
import com.project.server.service.emailservice.impl.EmailService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final StudentApplicationRepository studentApplicationRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public List<StudentApplicationDTO> fetchStudentApplications() {
        return studentApplicationRepository.findByStatus(StatusEnum.PENDING)
                .stream()
                .map(StudentApplication::convert)
                .toList();
    }

    @Transactional
    public void acceptApplicant(AcceptRequest request) {
        studentApplicationRepository.findById(request.applicationId()).ifPresentOrElse(
                application -> {
                    application.setStatus(StatusEnum.ACCEPTED);
                    studentApplicationRepository.save(application);
                },
                () -> {throw new EntityNotFoundException("APPLICATION_NOT_FOUND");}
        );
    }

    @Transactional
    public void rejectApplicant(DenyRequest request) {
        studentApplicationRepository.findById(request.applicationId()).ifPresentOrElse(
                application -> {
                    application.setStatus(StatusEnum.DENIED);
                    studentApplicationRepository.save(application);
                },
                () -> {throw new EntityNotFoundException("APPLICATION_NOT_FOUND");}
        );
    }

    @Transactional
    public List<CourseDTO> fetchCourses() {
        return courseRepository.findAll()
                .stream()
                .map(Course::convert)
                .toList();
    }

    @Transactional
    public void createModerator(CreateModeratorRequest request) {
        userRepository.findByUsername(request.username()).ifPresentOrElse(
                user -> {throw new EntityExistsException("USERNAME_EXISTS");},
                () -> userRepository.save(
                        User.builder()
                                .username(request.username())
                                .password(UUID.randomUUID().toString())
                                .email(request.email())
                                .role(RoleEnum.ROLE_COURSE_MODERATOR)
                                .enabled(false)
                                .build()
                )
        );
    }
}
