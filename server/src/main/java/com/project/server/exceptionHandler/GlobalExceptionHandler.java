package com.project.server.exceptionHandler;

import com.project.server.exception.RefreshTokenException;
import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<APIResponse<Void>> handleExpiredJWT(
            ExpiredJwtException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode("AUTH_TOKEN_EXPIRED")
                                .build()
                );
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<APIResponse<Void>> handleMalformedJWT(
            MalformedJwtException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.failed)
                                .internalCode("AUTH_TOKEN_MALFORMED")
                                .build()
                );
    }

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
    // Unknown Errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<Void>> runtimeErrorException(
            RuntimeException ex
    ) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(
                        APIResponse.<Void>builder()
                                .status(ResponseCode.server_error)
                                .internalCode("UNKNOWN_SERVER_ERROR")
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
                                .internalCode("INTEGRITY_VIOLATION")
                                .build()
                );
    }

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
}
