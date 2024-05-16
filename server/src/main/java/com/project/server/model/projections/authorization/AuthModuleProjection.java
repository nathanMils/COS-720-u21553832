package com.project.server.model.projections.authorization;

import java.util.List;
import java.util.UUID;

public interface AuthModuleProjection {
    UUID getId();
    List<AuthLectureProjection> getLectures();
}
