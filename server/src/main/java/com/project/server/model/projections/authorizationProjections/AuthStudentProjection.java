package com.project.server.model.projections.authorizationProjections;

public interface AuthStudentProjection {
    AuthModuleProjection getModule();
    AuthStudentCourseProjection getCourse();
}
