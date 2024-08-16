package com.candidate.pks.security;

import com.candidate.pks.exception.ErrorPOJA;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomAccessDeniedHandler implements AuthenticationEntryPoint {

    private boolean isAuthorizedUrl(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        List<String> authorizedURLs = Arrays.asList(
                "/swagger-ui/**",
                "/v3/**"
        );
        return authorizedURLs.stream().anyMatch(url -> new AntPathMatcher().match(url, requestURI));
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        if (isAuthorizedUrl(request)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorPOJA errorPOJO = new ErrorPOJA();
            errorPOJO.setCode("401");
            errorPOJO.setError_description("Bearer token is missing or invalid.");
            errorPOJO.setUser_description("Please provide a valid Bearer token.");
            ObjectMapper objectMapper = new ObjectMapper();
            String errorJson = objectMapper.writeValueAsString(errorPOJO);
            response.getWriter().write(errorJson);
        } else {
            throw authException;
        }
    }
}