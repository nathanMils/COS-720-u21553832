package com.project.server.request.admin;

import jakarta.validation.constraints.NotNull;

public record AcceptRequest(@NotNull Long applicationId) {
}
