package com.project.server.request.moduleModerator;

import com.project.server.constraint.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPostRequest {
    @NotNull
    @NotBlank(message = "id is mandatory")
    @ValidUUID
    private String moduleId;
    @NotNull
    @NotBlank(message = "content is mandatory")
    private String content;
}
