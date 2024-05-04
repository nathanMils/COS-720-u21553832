package com.project.server.filter;

import com.project.server.request.XSSWrapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Filter for handling Cross-Site Scripting (XSS) attacks.
 * This filter intercepts each request and wraps it with an XSSWrapper to sanitize the request parameters.
 */
@WebFilter("/*")
public class XSSFilter implements Filter {

    /**
     * Handles XSS attacks.
     * This method is called for each request. It wraps the request with an XSSWrapper to sanitize the request parameters,
     * and then passes the wrapped request along with the response to the next filter in the chain.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param chain the filter chain
     * @throws IOException if an input or output exception occurred
     * @throws ServletException if a servlet exception occurred
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XSSWrapper((HttpServletRequest) request), response);
    }
}