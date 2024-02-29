package com.project.server.controller;

import com.project.server.request.LoginRequest;
import com.project.server.request.RegistrationRequest;
import com.project.server.response.LoginResponse;
import com.project.server.response.RegistrationResponse;
import com.project.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService service;
    @PostMapping("/reg")
    public ResponseEntity<RegistrationResponse> registerStudent(
            @RequestBody RegistrationRequest request
    ) {
        return ResponseEntity.ok(service.registerStudent(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }
}
