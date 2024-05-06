package com.project.server.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler for AccessDeniedException.
 * This handler intercepts AccessDeniedException and sends a custom response to the client.
 */
@Component
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    /**
     * Handles AccessDeniedException.
     * This method is called whenever an AccessDeniedException is thrown.
     * It sends a custom response to the client indicating that access is denied.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param accessDeniedException the exception that was thrown
     * @throws IOException if an input or output exception occurred
     * @throws ServletException if a servlet exception occurred
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .status(ResponseCode.failed)
                .internalCode("ACCESS_DENIED")
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}