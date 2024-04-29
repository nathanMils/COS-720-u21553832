package com.project.server.service;

import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.ModuleDTO;
import com.project.server.model.entity.Course;
import com.project.server.model.entity.CourseModerator;
import com.project.server.model.entity.Module;
import com.project.server.model.entity.User;
import com.project.server.repository.CourseModeratorRepository;
import com.project.server.repository.CourseRepository;
import com.project.server.repository.ModuleRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.admin.CreateCourseRequest;
import com.project.server.request.courseModerator.CreateModuleRequest;
import com.project.server.response.courseModerator.FetchCourseResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseModeratorService {
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final CourseModeratorRepository courseModeratorRepository;

    public FetchCourseResponse fetchCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"));
        return FetchCourseResponse.builder()
                .courseName(course.getName())
                .courseDescription(course.getDescription())
                .courseModerator(course.getModerator().getUsername())
                .modules(
                        course.getModules()
                                .stream()
                                .map(Module::convert)
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Transactional
    public List<ModuleDTO> fetchModulesInCourse(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"))
                .getModules()
                .stream()
                .map(Module::convert)
                .collect(Collectors.toList());
    }
    @Transactional
    public void createModule(UUID courseId, CreateModuleRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"));
        moduleRepository.findByName(request.name()).ifPresent((module) -> {throw new EntityExistsException("MODULE_NAME_EXISTS");});
        System.out.println("Course: " + course.getName() + " " + course.getId());
        Module module = Module.builder()
                .name(request.name())
                .description(request.description())
                .course(course)
                .build();
        moduleRepository.save(module);
    }

    @Transactional
    public boolean deleteModule(
            UUID courseId,
            UUID moduleId
    ) {
        Module module = moduleRepository.findById(moduleId).orElseThrow(() -> new EntityNotFoundException("MODULE_NOT_FOUND"));
        if (!module.getCourse().getId().equals(courseId)) return false;
        moduleRepository.delete(module);
        return true;
    }

    @Transactional
    public CourseDTO createCourse(CreateCourseRequest request) {
        courseRepository.findByName(request.name()).ifPresent((course) -> {throw new EntityExistsException("COURSE_NAME_EXISTS");});
        Course course = courseRepository.save(
                Course.builder()
                        .name(request.name())
                        .description(request.description())
                        .moderator(getAuthenticatedUser())
                        .build()
        );
        courseModeratorRepository.save(
                CourseModerator.builder()
                        .user(getAuthenticatedUser())
                        .course(course)
                        .build()
        );
        return course.convert();
    }

    @Transactional
    public void deleteCourse(UUID courseId) {
        courseRepository.findById(courseId).ifPresentOrElse(
                courseRepository::delete,
                () -> {throw new EntityNotFoundException("COURSE_NOT_FOUND");}
        );
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.findByUsername(currentUserName).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        }
        throw new RuntimeException("ERROR");
    }
}
