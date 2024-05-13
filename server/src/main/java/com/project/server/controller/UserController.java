package com.project.server.controller;

import com.project.server.model.enums.RoleEnum;
import com.project.server.response.APIResponse;
import com.project.server.response.user.GetProfileResponse;
import com.project.server.service.AuthService;
import com.project.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<APIResponse<GetProfileResponse>> getProfile() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(userService.getProfile())
                );
    }

    @GetMapping("/role")
    public ResponseEntity<APIResponse<RoleEnum>> getRole() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(userService.getRole())
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
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }


    @PostMapping("/logout")
    public ResponseEntity<APIResponse<Void>> logout(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        servletResponse.setStatus(204);
        authService.logout(
                servletRequest,
                servletResponse
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success()
                );
    }
}
