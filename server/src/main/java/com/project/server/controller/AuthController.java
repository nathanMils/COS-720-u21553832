package com.project.server.controller;

import com.project.server.constraint.ValidUUID;
import com.project.server.exception.ConfirmationTokenException;
import com.project.server.model.dto.UserDTO;
import com.project.server.model.enums.RoleEnum;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.RefreshRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import com.project.server.response.auth.AuthResponse;
import com.project.server.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @Value("${app.token.authExpire}")
    private String authExpire;

    @Value("${app.token.refreshExpire}")
    private String refreshExpire;

    @Value("${app.cookie.sameSite}")
    private String sameSite;

    @Value("${app.cookie.secure}")
    private String secure;
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
    public ResponseEntity<APIResponse<UserDTO>> login(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            @RequestBody LoginRequest request
    ) {
        AuthResponse response = service.login(servletRequest,request);
        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            APIResponse.success(
                                    null,
                                    "INCORRECT_PASSWORD"
                            )
                    );
        }
        if (response.getVerified()) {
            setCookies(servletResponse,response);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success(
                                    response.getUserDTO(),
                                    "User logged in"
                            )
                    );
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            APIResponse.error(
                                    "EMAIL_NOT_VERIFIED"
                            )
                    );
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<UserDTO>> refresh(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        AuthResponse response = service.refresh(servletRequest);
        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            APIResponse.error(
                                    "TOKEN_INVALID"
                            )
                    );
        }
        setCookies(servletResponse,response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                response.getUserDTO(),
                                "SUCCESS"
                        )
                );
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<APIResponse<Void>> verifyCode(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            @Param("token") @ValidUUID String token
    ) {
        if (service.verifyToken(servletRequest,token)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success(
                                    null,
                                    "SUCCESS"
                            )
                    );
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            APIResponse.error("INVALID_TOKEN")
                    );
        }

    }

    private void setCookies(HttpServletResponse servletResponse, AuthResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken",response.getAccessToken())
                        .httpOnly(true)
                        .secure(Boolean.parseBoolean(secure))
                        .path("/")
                        .sameSite(sameSite)
                        .maxAge(Long.parseLong(authExpire))
                        .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",response.getRefreshToken())
                        .httpOnly(true)
                        .secure(Boolean.parseBoolean(secure))
                        .path("/")
                        .sameSite(sameSite)
                        .maxAge(Long.parseLong(refreshExpire))
                        .build();
        servletResponse.addHeader(HttpHeaders.SET_COOKIE,accessTokenCookie.toString());
        servletResponse.addHeader(HttpHeaders.SET_COOKIE,refreshTokenCookie.toString());
    }
}
