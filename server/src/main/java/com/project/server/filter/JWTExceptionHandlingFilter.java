package com.project.server.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.server.response.APIResponse;
import com.project.server.response.ResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for handling JWT exceptions.
 * This filter intercepts JWT exceptions and sends a custom response to the client.
 */
@Component
@Slf4j
public class JWTExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles JWT exceptions.
     * This method is called for each request. If a JWT exception is thrown, it is caught and handled.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet exception occurred
     * @throws IOException if an input or output exception occurred
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleJwtException(response, "JWT_EXPIRED", e);
        } catch (MalformedJwtException e) {
            handleJwtException(response, "JWT_MALFORMED", e);
        } catch (Exception e) {
            log.error("An error occurred while processing the request", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                    .status(ResponseCode.failed)
                    .internalCode("INTERNAL_SERVER_ERROR")
                    .build();
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        }
    }

    /**
     * Handles a JWT exception.
     * This method is called when a JWT exception is caught. It sends a custom response to the client.
     *
     * @param response the HTTP response
     * @param internalCode the internal code for the exception
     * @param e the exception that was caught
     * @throws IOException if an input or output exception occurred
     */
    private void handleJwtException(HttpServletResponse response, String internalCode, Exception e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .status(ResponseCode.failed)
                .internalCode(internalCode)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
