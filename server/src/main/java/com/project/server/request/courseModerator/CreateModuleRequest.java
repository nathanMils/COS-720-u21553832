package com.project.server.request.courseModerator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateModuleRequest(
        @NotNull @NotBlank(message = "username is mandatory") @Pattern(regexp = "[A-Za-z][A-Za-z0-9_ ]{7,29}$", message = "name not valid") String name,
        @NotNull @NotBlank(message = "description is mandatory") String description) {
}