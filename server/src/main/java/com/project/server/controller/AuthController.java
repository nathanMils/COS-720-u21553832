package com.project.server.controller;

import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.auth.LoginResponse;
import com.project.server.response.auth.ApplicationResponse;
import com.project.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/app/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    @PostMapping("/apply")
    public ResponseEntity<APIResponse<ApplicationResponse>> apply(
            @RequestBody ApplicationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                service.apply(request),
                                "SUCCESS"
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success(
                                    service.login(request),
                                    "User logged in"
                            )
                    );
        } catch (UsernameNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            APIResponse.error("EMAIL_NOT_FOUND")
                    );
        }
    }
}
