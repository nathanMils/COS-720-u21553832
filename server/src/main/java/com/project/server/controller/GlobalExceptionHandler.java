package com.project.server.controller;

import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {

    // Unknown Errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<Void>> runtimeErrorException(
            RuntimeException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.server_error)
                                .internalCode(String.format("Unknown Runtime Exception: %s",ex.getMessage()))
                                .build()
                );
    }

    // Database violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse<Void>> dataIntegrityViolationException(
            DataIntegrityViolationException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.server_error)
                                .internalCode(String.format("Unknown Data Integrity Violation: %s",ex.getMessage()))
                                .build()
                );
    }
}
