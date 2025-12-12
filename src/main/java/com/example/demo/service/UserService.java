package com.example.demo.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ---------------------------
    // Register User (Signup)
    // ---------------------------
    public User register(String username, String password, String fullName) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))  // bcrypt hash
                .fullName(fullName)
                .roles(Set.of(Role.ROLE_USER))
                .build();

        return userRepository.save(user);
    }

    // ---------------------------
    // Find User By Username (for login)
    // ---------------------------
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ---------------------------
    // UserDetailsService override
    // Required for Spring Security login flow
    // ---------------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getByUsername(username);

        // Convert Enum<Role> → Spring authorities
        List<SimpleGrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(r.name()))
                        .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
