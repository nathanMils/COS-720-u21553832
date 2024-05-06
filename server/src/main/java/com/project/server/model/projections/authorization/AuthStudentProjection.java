package com.project.server.model.projections.authorization;

public interface AuthStudentProjection {
    AuthModuleProjection getModule();
    AuthStudentCourseProjection getCourse();
}
