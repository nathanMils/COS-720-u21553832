package com.project.server.request.auth;

import jakarta.validation.constraints.NotNull;

public record ForgotPasswordRequest (
        @NotNull String username,
        @NotNull String email
) {
}
