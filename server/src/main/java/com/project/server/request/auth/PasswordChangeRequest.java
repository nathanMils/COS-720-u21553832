package com.project.server.request.auth;

import com.project.server.constraint.ValidPasswordConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PasswordChangeRequest(
        @ValidPasswordConstraint @NotNull @NotBlank(message = "password is mandatory") String password,
        @NotNull String token
) {
}
