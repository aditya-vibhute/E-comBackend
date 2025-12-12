package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ---------------------------
    // Signup
    // ---------------------------
    record SignupRequest(String username, String password, String fullName) {}

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {

        User created = userService.register(req.username(), req.password(), req.fullName());

        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully",
            "userId", created.getId(),
            "username", created.getUsername()
        ));
    }


    // ---------------------------
    // Login
    // ---------------------------
    record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
        }

        // Now get user from DB
        User user = userService.getByUsername(req.username());

        // Generate JWT containing userId + username + role
        String token = jwtUtil.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRoles().iterator().next().name()   // take first role
        );

        return ResponseEntity.ok(Map.of(
            "token", token,
            "userId", user.getId(),
            "username", user.getUsername(),
            "role", user.getRoles().iterator().next().name()
        ));
    }
}
