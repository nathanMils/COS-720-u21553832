package com.project.server.controller;

import com.project.server.exception.PasswordDoesNotMatchException;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.RefreshRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.auth.AuthResponse;
import com.project.server.service.AuthService;
import jakarta.validation.Valid;
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
    @PostMapping("/apply")
    public ResponseEntity<APIResponse<AuthResponse>> apply(
            @RequestBody @Valid ApplicationRequest request
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
    public ResponseEntity<APIResponse<AuthResponse>> login(
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
        } catch (PasswordDoesNotMatchException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            APIResponse.error("INCORRECT_PASSWORD")
                    );
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<AuthResponse>> refresh(
            @RequestBody RefreshRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                service.refresh(request),
                                "SUCCESS"
                        )
                );
    }
}
