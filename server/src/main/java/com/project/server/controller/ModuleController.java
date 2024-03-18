package com.project.server.controller;


import com.project.server.exception.ModuleDoesNotExistException;
import com.project.server.request.module.DeleteModuleRequest;
import com.project.server.request.module.FetchModuleRequest;
import com.project.server.response.APIResponse;
import com.project.server.response.module.DeleteModuleResponse;
import com.project.server.response.module.FetchModuleResponse;
import com.project.server.response.module.FetchModulesResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.server.exception.ModuleNameAlreadyExistsException;
import com.project.server.request.module.CreateModuleRequest;
import com.project.server.response.module.CreateModuleResponse;
import com.project.server.service.ModuleService;


@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/app/v1/module")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService service;

    @ExceptionHandler(ModuleDoesNotExistException.class)
    public ResponseEntity<APIResponse<Void>> moduleDoesNotExistException(
            ModuleDoesNotExistException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        APIResponse.error("MODULE_NOT_FOUND")
                );
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse<CreateModuleResponse>> createModule(
        @RequestBody CreateModuleRequest request
        ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            APIResponse.success(
                                    service.createModule(request),
                                    "MODULE_CREATED"
                            )
                    );
        } catch (ModuleNameAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                            APIResponse.error(
                                    "MODULE_ALREADY_EXISTS"
                            )
                    );
        }
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<APIResponse<DeleteModuleResponse>> deleteModule(
            @RequestBody DeleteModuleRequest request
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                service.deleteModule(request),
                                "MODULE_DELETED"
                        )
                );
    }
    
    @GetMapping("/fetchMany")
    public ResponseEntity<APIResponse<FetchModulesResponse>> fetchModules() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                service.fetchModules(),
                                "SUCCESS"
                        )
                );
    }

    @GetMapping("/fetch")
    public ResponseEntity<APIResponse<FetchModuleResponse>> fetchModule(
            @RequestBody FetchModuleRequest request
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                service.fetchModule(request),
                                "MODULE_FOUND"
                        )
                );
    }
}
