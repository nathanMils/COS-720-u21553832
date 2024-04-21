package com.project.server.service;

import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.ModuleDTO;
import com.project.server.model.dto.StudentApplicationDTO;
import com.project.server.model.entity.*;
import com.project.server.model.entity.Module;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.*;
import com.project.server.response.student.FetchModuleContentResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final StudentApplicationRepository studentApplicationRepository;
    private final PostRepository postRepository;

    @Transactional
    public List<StudentApplicationDTO> fetchStudentApplications() {
        return studentApplicationRepository.findByUserId(getAuthenticatedUser().getId())
                .stream()
                .map(StudentApplication::convert)
                .collect(Collectors.toList());
    }
    @Transactional
    public void apply(UUID courseId) {
        studentApplicationRepository.save(
                StudentApplication.builder()
                        .user(getAuthenticatedUser())
                        .course(
                                courseRepository.findById(courseId).orElseThrow(
                                        () -> new EntityNotFoundException("COURSE_NOT_FOUND")
                                )
                        )
                        .status(StatusEnum.PENDING)
                        .build()
        );
    }

    @Transactional
    public void drop(UUID courseId) {
        studentApplicationRepository.findByUserIdAndCourseId(
                getAuthenticatedUser().getId(),
                courseId
        ).ifPresentOrElse(
                (application) -> {
                    studentRepository.deleteAllByUserIdAndCourseId(getAuthenticatedUser().getId(), courseId);
                    studentApplicationRepository.delete(application);
                },
                () -> {
                    throw new EntityNotFoundException("APPLICATION_NOT_FOUND");
                }
        );
    }

    @Transactional
    public List<CourseDTO> fetchStudentCourses() {
        return studentApplicationRepository.findByUserIdAndStatus(getAuthenticatedUser().getId(), StatusEnum.ACCEPTED)
                .stream()
                .map(
                        application -> application.getCourse().convert()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ModuleDTO> fetchCourseModules(UUID courseId) {
        return moduleRepository.findByCourseId(courseId)
                .stream()
                .map(Module::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ModuleDTO> fetchStudentModules() {
        return studentRepository.findByUserId(getAuthenticatedUser().getId())
                .stream()
                .map(student -> student.getModule().convert())
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean registerStudent(UUID courseId, UUID moduleId) {
        User user = getAuthenticatedUser();
        if (studentRepository.findByUserIdAndModuleId(user.getId(),moduleId).isPresent()) {
            throw new EntityExistsException("STUDENT_ALREADY_REGISTERED");
        }
        // ignore course must exist for execution to get here
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"));

        if (!containsModule(course.getModules().stream().toList(),moduleId)) return false;
        Module module = moduleRepository.findById(moduleId).get();
        studentRepository.save(
                Student.builder()
                        .course(course)
                        .module(module)
                        .user(user)
                        .build()
        );
        return true;
    }

    @Transactional
    public void deRegisterStudent(UUID moduleId) {
        moduleRepository.findById(moduleId).ifPresentOrElse(
                (module) -> {
                    User user = getAuthenticatedUser();
                    studentRepository.findByUserIdAndModuleId(user.getId(),moduleId).ifPresentOrElse(
                            studentRepository::delete,
                            () -> {
                                throw new EntityNotFoundException("STUDENT_NOT_REGISTERED");
                            }
                    );
                },
                () -> {
                    throw new EntityNotFoundException("MODULE_NOT_FOUND");
                }
        );

    }

    @Transactional
    public FetchModuleContentResponse fetchModuleContent(UUID moduleId) {
        Module module = moduleRepository.findById(moduleId).orElseThrow(() -> new EntityNotFoundException("MODULE_NOT_FOUND"));
        return FetchModuleContentResponse.builder()
                .name(module.getName())
                .description(module.getDescription())
                .postDTOS(
                        postRepository.findByModuleId(moduleId)
                                .stream()
                                .map(Post::convert)
                                .collect(Collectors.toList())
                )
                .build();
    }
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.findByUsername(currentUserName).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        }
        throw new RuntimeException("ERROR");
    }

    private boolean containsModule(List<Module> modules,UUID moduleId) {
        for (Module module : modules) {
            if (module.getId().equals(moduleId)) {
                return true;
            }
        }
        return false;
    }
}
