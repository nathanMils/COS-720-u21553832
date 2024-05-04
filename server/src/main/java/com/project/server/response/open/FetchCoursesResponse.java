package com.project.server.response.open;

import com.project.server.model.dto.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchCoursesResponse {
    private List<CourseDTO> courseDTOS;
}
