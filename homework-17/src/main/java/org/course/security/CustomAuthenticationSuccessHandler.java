package org.course.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.course.domain.User;
import org.course.dto.GenericResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(GenericResponse.withUser(true, user.getName()));
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().print(content);
        response.getOutputStream().flush();
    }
}
