package edu.icet.demo.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String errorMessage;

        if (authException instanceof BadCredentialsException) {
            errorMessage = "Bad credentials. Please check your username and password.";
        } else {
            errorMessage = "Unauthorized access. Please login.";
        }

        response.getWriter().write(String.format("{\"error\": \"%s\"}", errorMessage));
    }
}