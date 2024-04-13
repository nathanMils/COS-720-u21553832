package com.project.server.request.auth;

import com.project.server.constraint.ValidPasswordConstraint;
import com.project.server.constraint.ValidUUID;
import jakarta.validation.constraints.*;
import lombok.*;



// Request is used to create entities => must conform to validation
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
    @NotNull
    @NotBlank(message = "username is mandatory")
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$", message = "username not valid")
    private String username;

    @NotNull
    @NotBlank(message = "password is mandatory")
    @ValidPasswordConstraint
    private String password;

    @NotNull
    @NotEmpty
    @NotBlank(message = "email is mandatory")
    @Email(message = "email is malformed")
    private String email;

    @NotNull
    @NotEmpty
    @NotBlank(message = "first name is mandatory")
    private String firstName;

    @NotNull
    @NotEmpty
    @NotBlank(message = "last name is mandatory")
    private String lastName;
}
