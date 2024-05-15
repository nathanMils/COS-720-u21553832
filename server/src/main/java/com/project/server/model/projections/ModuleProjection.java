package com.project.server.model.projections;

import com.project.server.model.entity.Post;

import java.util.List;

public interface ModuleProjection {
    String getName();
    String getDescription();
    List<Post> getPosts();
    List<LectureProjection> getLectures();
}
