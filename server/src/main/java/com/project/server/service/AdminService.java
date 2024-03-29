package com.project.server.service;

import com.project.server.model.entity.Course;
import com.project.server.model.entity.Module;
import com.project.server.model.entity.StudentApplication;
import com.project.server.model.enums.RoleEnum;
import com.project.server.model.entity.User;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.CourseRepository;
import com.project.server.repository.ModuleRepository;
import com.project.server.repository.StudentApplicationRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.admin.AcceptRequest;
import com.project.server.request.admin.CreateCourseRequest;
import com.project.server.request.admin.DeleteUserRequest;
import com.project.server.request.admin.DestroyCourseRequest;
import com.project.server.response.admin.CreateCourseResponse;
import com.project.server.response.admin.FetchStudentApplicationsResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final StudentApplicationRepository studentApplicationRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public FetchStudentApplicationsResponse fetchStudentApplications() {
        return FetchStudentApplicationsResponse.builder()
                .studentApplicationDTOS(
                        studentApplicationRepository.findByStatus(StatusEnum.PENDING)
                                .stream()
                                .map(StudentApplication::convert)
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Transactional
    public CreateCourseResponse createCourse(CreateCourseRequest request) {
        if (courseRepository.findByName(request.getName()).isPresent()) throw new EntityExistsException("COURSE_NAME_EXISTS");
        return CreateCourseResponse.builder()
                .courseDTO(
                        courseRepository.save(
                                Course.builder()
                                        .name(request.getName())
                                        .description(request.getDescription())
                                        .build()
                        ).convert()
                )
                .build();
    }

    @Transactional
    public Map<String,String> destroyCourse(DestroyCourseRequest request) {
        Map<String,String> warningList = new HashMap<>();
        Course course = courseRepository.findById(UUID.fromString(request.getCourseId())).orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"));
        for (Module module : course.getModules()) {
            if (module.getCourses().size() == 1) {
                if (request.isForce()) {
                    moduleRepository.deleteById(module.getId());
                } else {
                    warningList.put(
                            module.getName(),
                            "Will be deleted!"
                    );
                }
            }
        }
        return warningList;
    }

    @Transactional
    public void deleteUser(DeleteUserRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresentOrElse(
                userRepository::delete,
                () -> {
                    throw new UsernameNotFoundException("USER_NOT_FOUND");
                }
        );
    }

    @Transactional
    public void acceptApplicant(AcceptRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        studentApplicationRepository.findByUserIdAndCourseIdAndStatus(
                user.getId(),
                UUID.fromString(request.getCourseId()),
                StatusEnum.PENDING
        ).ifPresentOrElse(
                (application) -> {
                    application.setStatus(StatusEnum.ACCEPTED);
                    studentApplicationRepository.save(application);
                },
                () -> {
                    throw new EntityNotFoundException("APPLICATION_NOT_FOUND");
                }
        );
    }

    public void acceptCourseModerator(AcceptRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresentOrElse(
                (user) -> {
                    user.setRole(RoleEnum.ROLE_MODULE_MODERATOR);
                    userRepository.save(user);
                },
                () -> {throw new UsernameNotFoundException("USER_NOT_FOUND");}
        );
    }

}
