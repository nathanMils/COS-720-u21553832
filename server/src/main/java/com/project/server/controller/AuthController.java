package com.project.server.controller;

import com.project.server.constraint.Sanitize;
import com.project.server.constraint.ValidUUID;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.ForgotPasswordRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.request.auth.PasswordChangeRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.auth.AuthResponse;
import com.project.server.service.AuthService;
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
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for handling authentication related requests.
 */
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

    /**
     * Endpoint for handling application requests.
     *
     * @param servletRequest the HTTP request
     * @param request the application request
     * @return the response entity
     */
    @PostMapping("/apply")
    public ResponseEntity<APIResponse<Void>> apply(
            HttpServletRequest servletRequest,
            @RequestBody @Valid @Sanitize ApplicationRequest request
    ) {
        service.apply(servletRequest,request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );

    }

    /**
     * Endpoint for handling login requests.
     *
     * @param servletRequest the HTTP request
     * @param servletResponse the HTTP response
     * @param request the login request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<APIResponse<Void>> login(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            @RequestBody LoginRequest request
    ) {
        AuthResponse response = service.login(servletRequest,request);
        setCookies(servletResponse,response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );

    }

    /**
     * Endpoint for handling token refresh requests.
     *
     * @param servletRequest the HTTP request
     * @param servletResponse the HTTP response
     * @return the response entity
     */
    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<Void>> refresh(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        AuthResponse response = service.refresh(servletRequest);
        setCookies(servletResponse,response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }

    /**
     * Endpoint for verifying the email verification code.
     *
     * @param servletRequest the HTTP request
     * @param token the verification token
     * @return the response entity
     */
    @PostMapping("/verifyEmail")
    public ResponseEntity<APIResponse<Void>> verifyCode(
            HttpServletRequest servletRequest,
            @Param("token") @ValidUUID String token
    ) {
        service.verifyToken(servletRequest,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }

    /**
     * Endpoint for checking if the user is logged in.
     *
     * @param servletRequest the HTTP request
     * @return the response entity
     */
    @GetMapping("/loggedIn")
    public ResponseEntity<APIResponse<Void>> loggedIn(
            HttpServletRequest servletRequest
    ) {
        if(!service.isLoggedIn(servletRequest)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"UNAUTHORIZED");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }

    /**
     * Endpoint for handling password reset requests.
     *
     * @param servletRequest the HTTP request
     * @param request the password reset request
     * @return the response entity
     */
    @PostMapping("/forgot")
    public ResponseEntity<APIResponse<Void>> reset(
            HttpServletRequest servletRequest,
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        service.forgot(servletRequest,request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }

    /**
     * Endpoint for verifying the password reset token.
     *
     * @param servletRequest the HTTP request
     * @param token the password reset token
     * @return the response entity
     */
    @GetMapping("/isValid/{token}")
    public ResponseEntity<APIResponse<Void>> verifyToken(
            HttpServletRequest servletRequest,
            @PathVariable @ValidUUID String token
    ) {
        service.isValid(servletRequest,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }

    /**
     * Endpoint for handling password change requests.
     *
     * @param servletRequest the HTTP request
     * @param request the password change request
     * @return the response entity
     */
    @PostMapping("/reset")
    public ResponseEntity<APIResponse<Void>> changePassword(
            HttpServletRequest servletRequest,
            @RequestBody @Valid PasswordChangeRequest request
    ) {
        service.changePassword(servletRequest,request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }

    /**
     * Helper method for setting the access and refresh tokens as cookies in the response.
     *
     * @param servletResponse the HTTP response
     * @param response the authentication response containing the tokens
     */
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
