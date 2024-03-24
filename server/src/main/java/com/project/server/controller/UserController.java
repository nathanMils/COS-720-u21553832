package com.project.server.controller;

import com.project.server.request.user.ModuleRegisterRequest;
import com.project.server.response.user.ModuleRegisterResponse;
import com.project.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/app/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
}
