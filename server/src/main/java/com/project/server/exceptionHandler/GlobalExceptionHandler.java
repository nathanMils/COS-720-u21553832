package com.project.server.exceptionHandler;

import com.project.server.exception.RefreshTokenException;
import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public APIResponse<Void> validationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
                    if (errors.containsKey(error.getField())) {
                        errors.put(error.getField(), String.format("%s, %s", errors.get(error.getField()), error.getDefaultMessage()));
                    } else {
                        errors.put(error.getField(), error.getDefaultMessage());
                    }
                }
        );
        return APIResponse.<Void>builder()
                .status(ResponseCode.failed)
                .internalCode(errors.toString())
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(RefreshTokenException.class)
    public APIResponse<Void> tokenRefreshException(
            RefreshTokenException ex
    ) {
        return APIResponse.<Void>builder()
                .status(ResponseCode.failed)
                .internalCode(ex.getMessage())
                .build();
    }
    // Unknown Errors
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(RuntimeException.class)
    public APIResponse<Void> runtimeErrorException(
            RuntimeException ex
    ) {
        return APIResponse.<Void>builder()
                .status(ResponseCode.server_error)
                .internalCode("Unknown Server Error")
                .build();
    }

    // Database violations
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public APIResponse<Void> dataIntegrityViolationException(
            DataIntegrityViolationException ex
    ) {
        return APIResponse.<Void>builder()
                .status(ResponseCode.server_error)
                .internalCode("Unknown Data Integrity Violation")
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse<Void> usernameNotFound(
            UsernameNotFoundException usernameNotFoundException
    ) {
        return APIResponse.<Void>builder()
                .status(ResponseCode.failed)
                .internalCode("USERNAME_NOT_FOUND")
                .build();
    }
}
