package com.project.server.request.admin;

import com.project.server.constraint.ValidUUID;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DestroyCourseRequest {
    @NotNull
    private boolean force;
    @NotNull
    @ValidUUID
    private String courseId;
}
