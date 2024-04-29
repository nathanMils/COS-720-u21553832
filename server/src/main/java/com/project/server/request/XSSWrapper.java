package com.project.server.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.owasp.encoder.Encode;

/**
 * This class extends HttpServletRequestWrapper and overrides the getParameterValues method
 * to sanitize the request parameters using the OWASP Encoder library.
 * Note: This class does not sanitize JSON request bodies. To sanitize JSON bodies,
 * you would need to parse the JSON body into a Java object, sanitize the fields of that object,
 * and then use the sanitized object in your application.
 */
public class XSSWrapper extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XSSWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * This method retrieves the parameter values from the request, sanitizes them,
     * and then returns the sanitized values.
     *
     * @param parameter The name of the parameter to retrieve values for
     * @return An array of sanitized parameter values, or null if the parameter does not exist
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = sanitizeInput(values[i]);
        }
        return encodedValues;
    }

    /**
     * This method sanitizes a string input using the OWASP Encoder library.
     *
     * @param string The string to sanitize
     * @return The sanitized string
     */
    private String sanitizeInput(String string) {
        return Encode.forHtml(string);
    }
}