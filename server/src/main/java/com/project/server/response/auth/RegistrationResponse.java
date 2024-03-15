package com.project.server.response.auth;

import com.project.server.response.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {
    private Code code;
    private String token;
}
