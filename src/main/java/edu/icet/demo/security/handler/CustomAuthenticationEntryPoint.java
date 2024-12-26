package edu.icet.demo.security.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
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

        Exception jwtException = (Exception) request.getAttribute("exception");

        if (jwtException instanceof SignatureException) {
            errorMessage = "Invalid JWT signature. Please login again.";
        } else if (jwtException instanceof ExpiredJwtException) {
            errorMessage = "JWT has expired. Please login again.";
        } else if (jwtException instanceof MalformedJwtException) {
            errorMessage = "Malformed JWT token. Please provide a valid token.";
        } else if (authException instanceof BadCredentialsException) {
            errorMessage = "Bad credentials. Please check your username and password.";
        } else {
            System.out.println(jwtException);
            errorMessage = "Unauthorized access. Please login.";
        }

        response.getWriter().write(String.format("{\"error\": \"%s\"}", errorMessage));
    }
}