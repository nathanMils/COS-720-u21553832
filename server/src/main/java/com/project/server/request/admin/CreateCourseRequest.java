package com.project.server.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record CreateCourseRequest(
        @NotNull @NotBlank(message = "Name is mandatory") @Size(min = 3, message = "Name to small") @Size(max = 30, message = "Name to large") String name,
        @NotNull @NotBlank(message = "Description is mandatory") @Size(min = 10, message = "Description to small") @Size(max= 500, message= "Description too large") String description) {
}