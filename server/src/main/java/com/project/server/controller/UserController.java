package com.project.server.controller;

import com.project.server.model.enums.RoleEnum;
import com.project.server.response.APIResponse;
import com.project.server.response.user.GetProfileResponse;
import com.project.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<APIResponse<GetProfileResponse>> getProfile() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                userService.getProfile(),
                                "SUCCESS"
                        )
                );
    }

    @GetMapping("/role")
    public ResponseEntity<APIResponse<RoleEnum>> getRole() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                userService.getRole(),
                                "SUCCESS"
                        )
                );
    }

    @GetMapping("/test")
    public ResponseEntity<APIResponse<String>> test() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                "Test",
                                "SUCCESS"
                        )
                );
    }
}
