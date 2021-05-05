package org.course.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")){
            addUnauthenticatedJsonBody(response);
        } else {
            if (request.getRequestURI().startsWith("/api") || request.getRequestURI().startsWith("/datarest")){
                addUnauthenticatedJsonBody(response);
            } else {
                response.sendRedirect("/login");
            }
        }
    }

    private void addUnauthenticatedJsonBody(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(Collections.singletonMap("is_auth", false));
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().print(content);
        response.getOutputStream().flush();
    }
}
