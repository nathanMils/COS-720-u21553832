package com.project.server.controller;

import com.project.server.model.dto.UserDTO;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.auth.AuthResponse;
import com.project.server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest servletRequest;

    @Mock
    private HttpServletResponse servletResponse;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testApply_Success() {
        ApplicationRequest request = new ApplicationRequest();
        when(authService.apply(any(), any())).thenReturn(true);

        ResponseEntity<APIResponse<Void>> responseEntity = authController.apply(servletRequest, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("SUCCESS", Objects.requireNonNull(responseEntity.getBody()).getInternalCode());
    }

    @Test
    public void testApply_Conflict() {
        ApplicationRequest request = new ApplicationRequest();
        when(authService.apply(any(), any())).thenReturn(false);

        ResponseEntity<APIResponse<Void>> responseEntity = authController.apply(servletRequest, request);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("USERNAME_EXISTS", Objects.requireNonNull(responseEntity.getBody()).getInternalCode());
    }

    @Test
    @Disabled
    public void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        AuthResponse response = new AuthResponse();
        response.setVerified(true);
        when(authService.login(any(), any())).thenReturn(response);

        ResponseEntity<APIResponse<UserDTO>> responseEntity = authController.login(servletRequest, servletResponse, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User logged in", Objects.requireNonNull(responseEntity.getBody()).getInternalCode());
    }

    @Test
    public void testLogin_IncorrectPassword() {
        LoginRequest request = new LoginRequest();
        AuthResponse response = null; // Simulating incorrect password
        when(authService.login(any(), any())).thenReturn(response);

        ResponseEntity<APIResponse<UserDTO>> responseEntity = authController.login(servletRequest, servletResponse, request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("INCORRECT_PASSWORD", Objects.requireNonNull(responseEntity.getBody()).getInternalCode());
    }

    // Add tests for other scenarios in the login method

    @Test
    @Disabled
    public void testRefresh_Success() {
        AuthResponse response = new AuthResponse();
        response.setVerified(true);
        when(authService.refresh(any())).thenReturn(response);

        ResponseEntity<APIResponse<UserDTO>> responseEntity = authController.refresh(servletRequest, servletResponse);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("SUCCESS", Objects.requireNonNull(responseEntity.getBody()).getInternalCode());
    }

    @Test
    public void testRefresh_TokenInvalid() {
        AuthResponse response = null; // Simulating invalid token
        when(authService.refresh(any())).thenReturn(response);

        ResponseEntity<APIResponse<UserDTO>> responseEntity = authController.refresh(servletRequest, servletResponse);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("TOKEN_INVALID", Objects.requireNonNull(responseEntity.getBody()).getInternalCode());
    }

    // Add tests for other scenarios in the refresh method

    @Test
    public void testVerifyCode_Success() {
        String token = "valid_token";
        when(authService.verifyToken(any(), any())).thenReturn(true);

        ResponseEntity<APIResponse<Void>> responseEntity = authController.verifyCode(servletRequest,token);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("SUCCESS", Objects.requireNonNull(responseEntity.getBody()).getInternalCode());
    }

    @Test
    public void testVerifyCode_InvalidToken() {
        String token = "invalid_token";
        when(authService.verifyToken(any(), any())).thenReturn(false);

        ResponseEntity<APIResponse<Void>> responseEntity = authController.verifyCode(servletRequest, token);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("INVALID_TOKEN", Objects.requireNonNull(responseEntity.getBody()).getInternalCode());
    }
}