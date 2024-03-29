package com.project.server.response.admin;

import com.project.server.model.dto.StudentApplicationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchStudentApplicationsResponse {
    private List<StudentApplicationDTO> studentApplicationDTOS;
}