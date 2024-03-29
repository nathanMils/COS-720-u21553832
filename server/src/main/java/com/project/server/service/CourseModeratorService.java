package com.project.server.service;

import com.project.server.model.entity.Course;
import com.project.server.model.entity.Module;
import com.project.server.repository.CourseRepository;
import com.project.server.repository.ModuleRepository;
import com.project.server.request.courseModerator.CreateModuleRequest;
import com.project.server.request.courseModerator.RemoveModuleFromCourseRequest;
import com.project.server.response.courseModerator.CreateModuleResponse;
import com.project.server.response.module.FetchModulesResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseModeratorService {
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public FetchModulesResponse fetchModulesInCourse(UUID courseId) {
        return FetchModulesResponse.builder()
                .moduleDTOS(
                        courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"))
                                .getModules()
                                .stream()
                                .map(Module::convert)
                                .collect(Collectors.toList())
                )
                .build();
    }
    @Transactional
    public CreateModuleResponse createModule(CreateModuleRequest request) {
        Course course = courseRepository.findById(UUID.fromString(request.getCourseId()))
                .orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"));
        moduleRepository.findByName(request.getName()).ifPresent((module) -> {throw new EntityExistsException("MODULE_NAME_EXISTS");});
        Module module = Module.builder()
                .name(request.getName())
                .description(request.getDescription())
                .courses(new HashSet<>())
                .build();

        // Save the module without cascading to the course
        moduleRepository.save(module);

        // Since the relationship is bidirectional, ensure consistency
        course.getModules().add(module);
        courseRepository.save(course);

        return CreateModuleResponse.builder()
                .moduleDTO(module.convert())
                .build();
    }

    @Transactional
    public Map<String,String> removeModuleFromCourse(RemoveModuleFromCourseRequest request) {
        Map<String,String> warnings = new HashMap<>();
        List<Module> modules = moduleRepository.findByCoursesId(UUID.fromString(request.getCourseId()));
        if (!containsModule(modules, UUID.fromString(request.getModuleId()))) {
            throw new EntityExistsException("MODULE_NOT_IN_COURSE");
        }
        courseRepository.findById(UUID.fromString(request.getCourseId())).ifPresentOrElse(
                (course) -> {
                    moduleRepository.findById(UUID.fromString(request.getModuleId())).ifPresentOrElse(
                            (module) -> {
                                if (request.isForce()) {
                                    course.getModules().remove(module);
                                    courseRepository.save(course);

                                } else {
                                    warnings.put(module.getName(),"Will be deleted!");
                                }
                            },
                            () -> {
                                throw new EntityNotFoundException("MODULE_NOT_FOUND");
                            }
                    );
                },
                () -> {
                    throw new EntityNotFoundException("COURSE_NOT_FOUND");
                }
        );
        return warnings;
    }

    @Transactional
    public void destroyModule(
            UUID courseId,
            UUID moduleId
    ) {
        List<Module> modules = moduleRepository.findByCoursesId(courseId);
        if (!containsModule(modules,moduleId)) {
            throw new EntityExistsException("MODULE_NOT_IN_COURSE");
        }
        courseRepository.findById(courseId).ifPresentOrElse(
                (course) -> {
                    course.getModules().removeIf(
                            (module) -> {
                                return module.getId() == moduleId;
                            }
                    );
                    courseRepository.save(course);
                },
                () -> {
                    throw new EntityNotFoundException("COURSE_NOT_FOUND");
                }
        );
    }

    @Transactional
    public void addModuleToCourse(UUID courseId, UUID moduleId) {
        List<Module> modules = moduleRepository.findByCoursesId(courseId);
        if (containsModule(modules,moduleId)) {
            throw new EntityExistsException("MODULE_ALREADY_IN_COURSE");
        }
        courseRepository.findById(courseId).ifPresentOrElse(
                (course) -> {
                    moduleRepository.findById(moduleId).ifPresentOrElse(
                            (module) -> {
                                course.getModules().add(module);
                                courseRepository.save(course);
                            },
                            () -> {
                                throw new EntityNotFoundException("MODULE_NOT_FOUND");
                            }
                    );
                },
                () -> {
                    throw new EntityNotFoundException("COURSE_NOT_FOUND");
                }
        );
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
