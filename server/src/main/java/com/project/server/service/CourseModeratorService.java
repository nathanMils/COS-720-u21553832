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
    public CreateModuleResponse createModule(UUID courseId,CreateModuleRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"));
        moduleRepository.findByName(request.getName()).ifPresent((module) -> {throw new EntityExistsException("MODULE_NAME_EXISTS");});
        Module module = Module.builder()
                .name(request.getName())
                .description(request.getDescription())
                .course(course)
                .build();

        // Save the module without cascading to the course
        moduleRepository.save(module);

//        // Since the relationship is bidirectional, ensure consistency
//        course.getModules().add(module);
//        courseRepository.save(course);

        return CreateModuleResponse.builder()
                .moduleDTO(module.convert())
                .build();
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
}
