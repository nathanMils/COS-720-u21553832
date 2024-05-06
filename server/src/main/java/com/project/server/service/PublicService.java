package com.project.server.service;

import com.project.server.model.dto.CourseDTO;
import com.project.server.model.entity.Course;
import com.project.server.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicService {
    private final CourseRepository courseRepository;
    @Transactional
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(Course::convert)
                .toList();
    }
}
