package com.project.server.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private int numberOfCoursesTaken;
    private int numberOfCoursesCreated;
    private int numberOfModulesTaken;
    private int numberOfModulesCreated;
    private Date createdAt;
}
