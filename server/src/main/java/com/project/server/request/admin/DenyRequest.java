package com.project.server.request.admin;

import jakarta.validation.constraints.NotNull;

public record DenyRequest(@NotNull Long applicationId) {
}
