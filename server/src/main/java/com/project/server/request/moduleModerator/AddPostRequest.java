package com.project.server.request.moduleModerator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddPostRequest(@NotNull @NotBlank(message = "content is mandatory") String content) {
}
