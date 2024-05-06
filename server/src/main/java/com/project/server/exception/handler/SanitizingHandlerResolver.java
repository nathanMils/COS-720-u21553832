package com.project.server.exception.handler;

import com.project.server.constraint.Sanitize;
import org.owasp.encoder.Encode;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class SanitizingHandlerResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Sanitize.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object argument = binderFactory.createBinder(webRequest, null, Objects.requireNonNull(parameter.getParameterName())).convertIfNecessary(webRequest.getParameter(parameter.getParameterName()), parameter.getParameterType());
        if (argument instanceof String string) {
            return Encode.forHtml(string);
        }
        // Add sanitization logic for other types if needed
        return argument;
    }
}