package com.project.server.controller;

import com.project.server.constraint.ValidUUID;
import com.project.server.response.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/api/v1/moduleModerator")
@RequiredArgsConstructor
public class ModuleModeratorController {


}
