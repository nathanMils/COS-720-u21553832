package com.project.server.request.courseModerator;

import com.project.server.constraint.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateModuleRequest {

    @NotNull
    @NotBlank(message = "username is mandatory")
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$", message = "name not valid")
    private String name;

    @NotNull
    @NotBlank(message = "description is mandatory")
    private String description;

    @NotNull
    @ValidUUID
    private String courseId;
}
