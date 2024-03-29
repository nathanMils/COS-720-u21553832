package com.project.server.request.admin;

import com.project.server.constraint.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptRequest {
    @NotNull
    @NotBlank(message = "username is mandatory")
    private String username;
    @NotNull
    @NotBlank(message = "courseId is mandatory")
    @ValidUUID
    private String courseId;
}
