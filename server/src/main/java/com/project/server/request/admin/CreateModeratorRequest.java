package com.project.server.request.admin;

import jakarta.validation.constraints.*;

public record CreateModeratorRequest(
        @NotNull @NotBlank(message = "username is mandatory") @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$", message = "username not valid") String username,
        @NotNull @NotEmpty @NotBlank(message = "email is mandatory") @Email(message = "email is malformed") String email,
        @NotNull @NotEmpty @NotBlank(message = "first name is mandatory") String firstName,
        @NotNull @NotEmpty @NotBlank(message = "last name is mandatory") String lastName
) {
}
