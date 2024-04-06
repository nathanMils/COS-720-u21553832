package com.project.server.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseRequest {
    @NotNull
    @NotBlank(message = "Name is mandatory")
    @Size(min = 6, message = "Name to small")
    @Size(max = 30, message = "Name to large")
    private String name;
    @NotNull
    @NotBlank(message = "Description is mandatory")
    private String description;
}