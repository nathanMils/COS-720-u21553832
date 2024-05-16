package com.project.server.model.projections;

import java.util.Date;
import java.util.UUID;

public interface LectureProjection {
    UUID getId();
    String getFileName();
    Date getCreatedAt();
}
