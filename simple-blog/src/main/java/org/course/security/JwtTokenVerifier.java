package org.course.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.course.configuration.BlogConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final BlogConfiguration blogConfiguration;

    //todo add expiration check
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String key = blogConfiguration.getAccessKey();

        String authorizationHeader = request.getHeader("Authorization");

        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            filterChain.doFilter(request, response);
            return;
        }

        Cookie authCookie = getAuthCookieFromRequest(cookies);

        if (authCookie == null || authCookie.getValue().equals("")){
            filterChain.doFilter(request, response);
            return;
        }

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = getWholeTokenFromCookieAndHeaderParts(authorizationHeader, authCookie);

        try{

            validateTokenOrElseThrow(key, token);

        } catch (JwtException e){
            //todo add some logic when invalid request has come, may be log or ...
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void validateTokenOrElseThrow(String key, String token) {

        Claims body = parseToken(key, token);

        Authentication authentication = extractAuthenticationDataFromTokenBody(body);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Claims parseToken(String key, String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().
                setSigningKey(Keys.hmacShaKeyFor(key.getBytes())).build()
                .parseClaimsJws(token);

        return claimsJws.getBody();
    }

    private Authentication extractAuthenticationDataFromTokenBody(Claims body) {

        String username = body.getSubject();

        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

        Set<SimpleGrantedAuthority> grantedAuthoritySet = authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.get("authority"))).collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(username, null, grantedAuthoritySet);
    }

    private String getWholeTokenFromCookieAndHeaderParts(String authorizationHeader, Cookie authCookie) {
        String sign = authCookie.getValue().replace("Bearer_", "");

        String algoAndClaim = authorizationHeader.replace("Bearer ", "");

        return algoAndClaim + "." + sign;
    }

    private Cookie getAuthCookieFromRequest(Cookie[] cookies) {
        Cookie authCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) authCookie = cookie;
        }
        return authCookie;
    }
}
