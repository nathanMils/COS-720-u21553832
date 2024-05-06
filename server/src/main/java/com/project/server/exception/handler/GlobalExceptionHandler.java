package com.project.server.exception.handler;

import com.project.server.exception.*;
import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


/**
 * Global exception handler for the application.
 * This class handles all the exceptions thrown across the application and sends appropriate responses.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles ResponseStatusException.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<APIResponse<Void>> handleResponseStatusException(
            ResponseStatusException ex
    ) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode(ex.getReason())
                                .build()
                );
    }

    /**
     * Handles AuthenticationException.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        APIResponse.<String>builder()
                                .status(ResponseCode.failed)
                                .internalCode("AUTHENTICATION_FAILED")
                                .data(ex.getMessage())
                                .build()
                );
    }

    /**
     * Handles EntityExistsException.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<APIResponse<Void>> entityExists(
            EntityExistsException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode(ex.getMessage())
                                .build()
                );
    }

    /**
     * Handles EntityNotFoundException.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> entityNotFound(
            EntityNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ModuleNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> moduleNotFound(
            ModuleNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> courseNotFound(
            CourseNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<APIResponse<Void>> invalidToken(
            InvalidTokenException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode(ex.getMessage())
                                .build()
                );
    }

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> validationExceptions(
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
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode("VALIDATION_FAILED")
                                .errors(errors)
                                .build()
                );
    }

    /**
     * Handles RefreshTokenException.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<APIResponse<Void>> tokenRefreshException(
            RefreshTokenException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode(ex.getMessage())
                                .build()
                );

    }

    /**
     * Handles DataIntegrityViolationException.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse<Void>> dataIntegrityViolationException(
            DataIntegrityViolationException ex
    ) {
        log.atError().log("Data integrity violation exception: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.server_error)
                                .internalCode("INTEGRITY_VIOLATION")
                                .build()
                );
    }

    @ExceptionHandler(CryptographicException.class)
    public ResponseEntity<APIResponse<Void>> encryptionException(
            CryptographicException ex
    ) {
        log.atError().log("Cryptographic exception: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.server_error)
                                .internalCode("CRYPTOGRAPHIC_ERROR")
                                .build()
                );
    }

    /**
     * Handles UsernameNotFoundException.
     *
     * @param usernameNotFoundException the exception
     * @return the response entity
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> usernameNotFound(
            UsernameNotFoundException usernameNotFoundException
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode("USER_NOT_FOUND")
                                .build()
                );
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<APIResponse<Void>> invalidUser(
            InvalidUserException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode(ex.getMessage())
                                .build()
                );
    }
}
