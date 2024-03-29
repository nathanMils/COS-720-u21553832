package com.project.server.request.courseModerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveModuleFromCourseRequest {
    private String courseId;
    private String moduleId;
    private boolean force;
}
