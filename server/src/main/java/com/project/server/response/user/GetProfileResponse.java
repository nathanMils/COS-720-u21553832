package com.project.server.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
