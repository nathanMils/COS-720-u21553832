package com.project.server.request.moduleModerator;

import com.project.server.constraint.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddPostRequest(@ValidUUID @NotNull @NotBlank(message = "id is mandatory") String moduleId,
                             @NotNull @NotBlank(message = "content is mandatory") String content) {
}
