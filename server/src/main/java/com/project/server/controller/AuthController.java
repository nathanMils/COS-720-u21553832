package com.project.server.controller;

import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.RegistrationRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.auth.LoginResponse;
import com.project.server.response.auth.RegistrationResponse;
import com.project.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/app/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    @PostMapping("/reg")
    public ResponseEntity<APIResponse<RegistrationResponse>> registerStudent(
            @RequestBody RegistrationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                service.registerStudent(request),
                                String.format("User '%s' successfully created",request.getUsername())
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                service.login(request),
                                "User logged in"
                        )
                );
    }
}
