package com.project.server.component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * This class is a custom authentication entry point for the application.
 * It is used to handle authentication exceptions and provide a custom response.
 */
@Component("delegatedAuthEntryPoint")
public class AuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * The HandlerExceptionResolver is used to resolve exceptions that are thrown during the handling of HTTP requests.
     */
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * This method is called whenever an exception is thrown due to an unauthenticated user trying to access a resource that requires authentication.
     * The method delegates the handling of the exception to the HandlerExceptionResolver.
     *
     * @param request that resulted in an AuthenticationException
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        resolver.resolveException(request,response,null,authException);
    }
}