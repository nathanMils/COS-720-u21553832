package com.project.server.response.admin;

import com.project.server.model.dto.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseResponse {
    private CourseDTO courseDTO;
}
