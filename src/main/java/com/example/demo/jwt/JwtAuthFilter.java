package com.example.demo.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.security.AuthUser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (!jwtUtil.isValid(token)) {
            chain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.extractUsername(token);
        Long userId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);

        // Build Spring security auth object
        var authorities = List.of(new SimpleGrantedAuthority(role));

        var authToken = new UsernamePasswordAuthenticationToken(
                new AuthUser(userId, username, role), // principal
                null,
                authorities
        );

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // Set authentication
        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(request, response);
    }
}
