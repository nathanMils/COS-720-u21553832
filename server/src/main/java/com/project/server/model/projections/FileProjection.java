package com.project.server.model.projections;

public interface FileProjection {
    String getName();
    String getDescription();
    byte[] getContent();
}
