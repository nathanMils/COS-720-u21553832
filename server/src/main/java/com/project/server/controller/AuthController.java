package com.project.server.controller;

import com.project.server.constraint.ValidUUID;
import com.project.server.exception.ConfirmationTokenException;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.RefreshRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.auth.AuthResponse;
import com.project.server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    @PostMapping("/apply")
    public ResponseEntity<APIResponse<Void>> apply(
            HttpServletRequest servletRequest,
            @RequestBody @Valid ApplicationRequest request
    ) {
        if (service.apply(servletRequest,request)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success(
                                    null,
                                    "SUCCESS"
                            )
                    );
        } else {
            return  ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                            APIResponse.error("USERNAME_EXISTS")
                    );
        }

    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponse>> login(
            HttpServletRequest servletRequest,
            @RequestBody LoginRequest request
    ) {
        AuthResponse response = service.login(servletRequest,request);
        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            APIResponse.success(
                                    response,
                                    "INCORRECT_PASSWORD"
                            )
                    );
        }
        if (response.getVerified())
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success(
                                    response,
                                    "User logged in"
                            )
                    );
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.error(
                                    "EMAIL_NOT_VERIFIED"
                            )
                    );
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<AuthResponse>> refresh(
            HttpServletRequest servletRequest,
            @RequestBody RefreshRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                service.refresh(servletRequest,request),
                                "SUCCESS"
                        )
                );
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<APIResponse<?>> verifyCode(
            HttpServletRequest servletRequest,
            @Param("token") @ValidUUID String token
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success(
                                    service.verifyToken(servletRequest,token),
                                    "SUCCESS"
                            )
                    );
        } catch (ConfirmationTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            APIResponse.error(
                                    e.getMessage()
                            )
                    );
        }
    }
}
