package org.course.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.course.api.request.UsernameAndPasswordAuthenticationRequest;
import org.course.configuration.BlogConfiguration;
import org.course.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final BlogConfiguration blogConfiguration;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      BlogConfiguration blogConfiguration) {
        this.authenticationManager = authenticationManager;
        this.blogConfiguration = blogConfiguration;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);


            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword());

            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String key = blogConfiguration.getAccessKey();
        String refreshKey = blogConfiguration.getRefreshKey();

        User user = (User) authResult.getPrincipal();

        String accessToken = Jwts.builder()
                .setSubject(user.getId())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(user.getId())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(refreshKey.getBytes()))
                .compact();

        String[] accessTokenParts = accessToken.split("\\.");
        String[] refreshTokenParts = refreshToken.split("\\.");

        Cookie accessCookie = new Cookie("Authorization", "Bearer_" + accessTokenParts[2]);
        accessCookie.setHttpOnly(true);
//        accessCookie.setSecure(true);
//        accessCookie.setPath("");
//        accessCookie.setDomain("");

        Cookie refreshCookie = new Cookie("Refresh", "Bearer_" + refreshTokenParts[2]);
        refreshCookie.setHttpOnly(true);
//        refreshCookie.setSecure(true);
//        refreshCookie.setPath("");
//        refreshCookie.setDomain("");

        response.addHeader("Authorization", "Bearer " + accessTokenParts[0] + "." + accessTokenParts[1]);
        response.addHeader("Refresh", "Bearer " + refreshTokenParts[0] + "." + refreshTokenParts[1]);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

    }

}
