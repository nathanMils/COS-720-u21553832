package com.project.server.response.user;

import com.project.server.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuleRegisterResponse {
    private ResponseCode code;
}
