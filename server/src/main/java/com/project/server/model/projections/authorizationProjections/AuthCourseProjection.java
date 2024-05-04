package com.project.server.model.projections.authorizationProjections;

import java.util.Set;
import java.util.UUID;

public interface AuthCourseProjection {
    UUID getId();
    Set<AuthModuleProjection> getModules();
}
