package com.project.server.model.projections.auth;

import com.project.server.model.enums.RoleEnum;

public interface UserAuthProjection {
    Long getId();
    String getUsername();
    String getPassword();
    RoleEnum getRole();
    String getEmail();
    Boolean getEnabled();
}
