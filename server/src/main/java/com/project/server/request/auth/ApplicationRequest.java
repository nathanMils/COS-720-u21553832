package com.project.server.request.auth;

import com.project.server.constraint.ValidPasswordConstraint;
import jakarta.validation.constraints.*;


// Request is used to create entities => must conform to validation constraints
public record ApplicationRequest(
        @NotNull @NotBlank(message = "username is mandatory") @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$", message = "username not valid") String username,
        @ValidPasswordConstraint @NotNull @NotBlank(message = "password is mandatory") String password,
        @NotNull @NotEmpty @NotBlank(message = "email is mandatory") @Email(message = "email is malformed") String email,
        @NotNull @NotEmpty @NotBlank(message = "first name is mandatory") String firstName,
        @NotNull @NotEmpty @NotBlank(message = "last name is mandatory") String lastName) {
}
