package com.project.server.service;

import com.project.server.exception.InvalidUserException;
import com.project.server.exception.ModuleNotFoundException;
import com.project.server.model.dto.CourseDTO;
import com.project.server.model.dto.LectureDTO;
import com.project.server.model.dto.ModuleDTO;
import com.project.server.model.dto.StudentApplicationDTO;
import com.project.server.model.entity.*;
import com.project.server.model.entity.Module;
import com.project.server.model.enums.StatusEnum;
import com.project.server.model.projections.ModuleProjection;
import com.project.server.repository.*;
import com.project.server.response.student.FetchLectureResponse;
import com.project.server.response.student.FetchModuleContentResponse;
import com.project.server.response.student.FetchStudentCourseResponse;
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
    private final LectureRepository lectureRepository;

    public FetchStudentCourseResponse fetchCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"));
        List<Module> modules = studentRepository.findByUserIdAndCourseId(getAuthenticatedUser().getId(),courseId)
                .stream()
                .map(Student::getModule)
                .toList();
        return FetchStudentCourseResponse.builder()
                .courseName(course.getName())
                .courseDescription(course.getDescription())
                .modules(
                        course.getModules().stream()
                                .map(
                                        module -> module.convert().convert(modules.contains(module))
                                )
                                .collect(Collectors.toList())
                )
                .courseModerator(course.getModerator().getUsername())
                .build();
    }

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
                application -> {
                    studentRepository.deleteAllByUserIdAndCourseId(getAuthenticatedUser().getId(), courseId);
                    studentApplicationRepository.delete(application);
                },
                () -> {
                    throw new EntityNotFoundException("APPLICATION_NOT_FOUND");
                }
        );
    }

    @Transactional
    public void dropApplication(Long applicationId) {
        studentApplicationRepository.findById(applicationId).ifPresentOrElse(
                application -> {
                    studentRepository.deleteAllByUserIdAndCourseId(application.getUser().getId(), application.getCourse().getId());
                    studentApplicationRepository.delete(application);
                },
                () -> {
                    throw new EntityNotFoundException("APPLICATION_NOT_FOUND");
                }
        );
    }

    public List<CourseDTO> fetchStudentCourses() {
        return studentApplicationRepository.findByUserIdAndStatus(getAuthenticatedUser().getId(), StatusEnum.ACCEPTED)
                .stream()
                .map(
                        application -> application.getCourse().convert()
                )
                .toList();
    }

    public List<CourseDTO> fetchOtherCourses() {
        return courseRepository.findAll()
                .stream()
                .filter(
                        course -> studentApplicationRepository.findByUserIdAndCourseId(
                                getAuthenticatedUser().getId(),
                                course.getId()
                        ).isEmpty()
                )
                .map(Course::convert)
                .toList();
    }

    @Transactional
    public List<ModuleDTO> fetchCourseModules(UUID courseId) {
        return moduleRepository.findByCourseId(courseId)
                .stream()
                .map(Module::convert)
                .toList();
    }

    @Transactional
    public List<ModuleDTO> fetchStudentModules() {
        return studentRepository.findByUserId(getAuthenticatedUser().getId())
                .stream()
                .map(student -> student.getModule().convert())
                .toList();
    }

    @Transactional
    public boolean registerStudent(UUID courseId, UUID moduleId) {
        User user = getAuthenticatedUser();
        if (studentRepository.findByUserIdAndModuleId(user.getId(),moduleId).isPresent()) {
            throw new EntityExistsException("STUDENT_ALREADY_REGISTERED");
        }
        // ignore course must exist for execution to get here
        Course course = courseRepository.findById(courseId).orElseThrow(ModuleNotFoundException::new);

        if (!containsModule(course.getModules().stream().toList(),moduleId)) return false;
        Module module = moduleRepository.findById(moduleId).orElseThrow(ModuleNotFoundException::new);
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
                module -> {
                    User user = getAuthenticatedUser();
                    studentRepository.findByUserIdAndModuleId(user.getId(),moduleId).ifPresentOrElse(
                            studentRepository::delete,
                            () -> {
                                throw new EntityNotFoundException("STUDENT_NOT_REGISTERED");
                            }
                    );
                },
                () -> {
                    throw new ModuleNotFoundException();
                }
        );

    }

    @Transactional
    public FetchModuleContentResponse fetchModuleContent(UUID moduleId) {
        ModuleProjection module = moduleRepository.findModuleProjectionById(moduleId).orElseThrow(() -> new EntityNotFoundException("MODULE_NOT_FOUND"));
        return FetchModuleContentResponse.builder()
                .name(module.getName())
                .description(module.getDescription())
                .postDTOS(
                        postRepository.findByModuleId(moduleId)
                                .stream()
                                .map(Post::convert)
                                .toList()
                )
                .lectures(
                        module.getLectures()
                                .stream()
                                .map(lecture -> new LectureDTO(lecture.getId(),lecture.getFileName(),lecture.getCreatedAt()))
                                .toList()
                )
                .build();
    }

    @Transactional
    public  Lecture fetchLecture(UUID lectureId) {
        return lectureRepository.findById(lectureId).orElseThrow(() -> new EntityNotFoundException("LECTURE_NOT_FOUND"));
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.findByUsername(currentUserName).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        }
        throw new InvalidUserException();
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
