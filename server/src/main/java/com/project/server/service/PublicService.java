package com.project.server.service;

import com.project.server.model.entity.Course;
import com.project.server.repository.CourseRepository;
import com.project.server.response.open.FetchCoursesResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicService {
    private final CourseRepository courseRepository;
    @Transactional
    public FetchCoursesResponse getAllCourses() {
        return FetchCoursesResponse.builder()
                .courseDTOS(
                        courseRepository.findAll()
                                .stream()
                                .map(Course::convert)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
