package com.example.demo.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    private final JwtProperties props;
    private Key key;

    public JwtUtil(JwtProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes());
    }

    // -----------------------------
    // Generate JWT for E-commerce
    // -----------------------------
    public String generateToken(Long userId, String username, String role) {

        return Jwts.builder()
                .setSubject(username)               // sub
                .claim("userId", userId)            // custom claim
                .claim("role", role)                // custom claim
                .setIssuedAt(new Date())            // iat
                .setExpiration(new Date(System.currentTimeMillis() + props.getExpirationMs()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // -----------------------------
    // Extract Claims
    // -----------------------------
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // -----------------------------
    // Validate Token
    // -----------------------------
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // -----------------------------
    // Internal parser
    // -----------------------------
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
