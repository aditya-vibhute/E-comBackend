package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddressResponse;
import com.example.demo.dto.ProfilePageResponse;
import com.example.demo.dto.UserProfileRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.AddressService;
import com.example.demo.service.UserProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserProfileService profileService;
    private final AddressService addressService;
    private final JwtUtil jwtService;

    public ProfileController(UserProfileService profileService,
                             AddressService addressService,
                             JwtUtil jwtService) {
        this.profileService = profileService;
        this.addressService = addressService;
        this.jwtService = jwtService;
    }

    // Profile page → profile + addresses
    @GetMapping
    public ResponseEntity<ProfilePageResponse> getProfile(
            @RequestHeader("Authorization") String auth) {

        Long userId = jwtService.extractUserId(auth.substring(7));

        UserProfileResponse profile = profileService.getProfile(userId);
        List<AddressResponse> addresses = addressService.getUserAddresses(userId);

        return ResponseEntity.ok(
                new ProfilePageResponse(profile, addresses)
        );
    }

    // Save / update profile
    @PostMapping
    public ResponseEntity<UserProfileResponse> saveProfile(
            @RequestHeader("Authorization") String auth,
            @RequestBody UserProfileRequest req) {

        Long userId = jwtService.extractUserId(auth.substring(7));
        return ResponseEntity.ok(profileService.saveOrUpdateProfile(userId, req));
    }
}
