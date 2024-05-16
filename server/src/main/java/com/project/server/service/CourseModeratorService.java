package com.project.server.service;

import com.project.server.exception.CourseNotFoundException;
import com.project.server.exception.InvalidUserException;
import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.LectureDTO;
import com.project.server.model.dto.ModuleDTO;
import com.project.server.model.entity.*;
import com.project.server.model.entity.Module;
import com.project.server.model.projections.ModuleProjection;
import com.project.server.repository.CourseModeratorRepository;
import com.project.server.repository.CourseRepository;
import com.project.server.repository.ModuleRepository;
import com.project.server.repository.UserRepository;
import com.project.server.request.admin.CreateCourseRequest;
import com.project.server.request.moderator.CreateModuleRequest;
import com.project.server.response.moderator.FetchCourseResponse;
import com.project.server.response.student.FetchModuleContentResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseModeratorService {
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final CourseModeratorRepository courseModeratorRepository;

    public FetchCourseResponse fetchCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
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
        return courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new)
                .getModules()
                .stream()
                .map(Module::convert)
                .collect(Collectors.toList());
    }
    @Transactional
    public void createModule(UUID courseId, CreateModuleRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        moduleRepository.findByName(request.name()).ifPresent(module -> {throw new EntityExistsException("MODULE_NAME_EXISTS");});
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
        courseRepository.findByName(request.name()).ifPresent(course -> {throw new EntityExistsException("COURSE_NAME_EXISTS");});
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
                course -> {
                    courseRepository.delete(course);
                    log.atInfo().log(
                            "Course {} deleted",
                            course.getName()
                    );
                },
                () -> {throw new CourseNotFoundException();}
        );
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.findByUsername(currentUserName).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        }
        throw new InvalidUserException();
    }

    @Transactional
    public FetchModuleContentResponse fetchModule(UUID moduleId) {
        ModuleProjection module = moduleRepository.findModuleProjectionById(moduleId).orElseThrow(() -> new EntityNotFoundException("MODULE_NOT_FOUND"));
        return FetchModuleContentResponse.builder()
                .name(module.getName())
                .description(module.getDescription())
                .postDTOS(
                        module.getPosts()
                                .stream()
                                .map(Post::convert)
                                .collect(Collectors.toList())
                )
                .lectures(
                        module.getLectures()
                                .stream()
                                .map(lecture -> new LectureDTO(lecture.getId(),lecture.getFileName(),lecture.getCreatedAt()))
                                .toList()
                )
                .build();
    }
}
