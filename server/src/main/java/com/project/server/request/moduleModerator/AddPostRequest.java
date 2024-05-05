package com.project.server.request.moduleModerator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddPostRequest(@NotNull @NotBlank(message = "content is mandatory") @Size(min = 3, message = "post to small") @Size(max = 200, message = "post to large") String content) {
}
