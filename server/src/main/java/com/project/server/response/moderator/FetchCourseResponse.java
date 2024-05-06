package com.project.server.response.moderator;

import com.project.server.model.dto.ModuleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchCourseResponse {
    private String courseName;
    private String courseDescription;
    private String courseModerator;
    private List<ModuleDTO> modules;
}
