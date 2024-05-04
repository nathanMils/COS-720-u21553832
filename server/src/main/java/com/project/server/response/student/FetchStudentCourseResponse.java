package com.project.server.response.student;

import com.project.server.model.dto.StudentModuleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchStudentCourseResponse {
    private String courseName;
    private String courseDescription;
    private String courseModerator;
    private List<StudentModuleDTO> modules;
}
