package com.project.server.service;

import com.project.server.model.dto.ModuleDTO;
import com.project.server.model.entity.Course;
import com.project.server.model.entity.Module;
import com.project.server.repository.CourseRepository;
import com.project.server.repository.ModuleRepository;
import com.project.server.request.courseModerator.CreateModuleRequest;
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
    public List<ModuleDTO> fetchModulesInCourse(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"))
                .getModules()
                .stream()
                .map(Module::convert)
                .collect(Collectors.toList());
    }
    @Transactional
    public ModuleDTO createModule(UUID courseId, CreateModuleRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("COURSE_NOT_FOUND"));
        moduleRepository.findByName(request.name()).ifPresent((module) -> {throw new EntityExistsException("MODULE_NAME_EXISTS");});
        System.out.println("Course: " + course.getName() + " " + course.getId());
        Module module = Module.builder()
                .name(request.name())
                .description(request.description())
                .course(course)
                .build();
        return moduleRepository.save(module).convert();
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
