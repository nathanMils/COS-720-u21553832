package com.project.server.model.dto;

import com.project.server.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentApplicationDTO {
    private Long applicationId;
    private UUID courseId;
    private String username;
    private String userFirstName;
    private String userLastName;
    private String courseName;
    private String description;
    private StatusEnum status;
}
