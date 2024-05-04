package com.project.server.model.projections.authorizationProjections;

import com.project.server.model.enums.RoleEnum;

public interface AuthUserProjection {
    Long getId();
    String getUsername();
    String getPassword();
    Boolean getEnabled();
    RoleEnum getRole();
}
