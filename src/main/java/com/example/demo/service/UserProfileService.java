package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.dto.UserProfileRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository profileRepo;

    public UserProfileService(UserProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    public UserProfileResponse getProfile(Long userId) {

        return profileRepo.findByUserId(userId)
                .map(p -> new UserProfileResponse(
                        p.getFullName(),
                        p.getGender(),
                        p.getAge(),
                        p.getPhone(),
                        p.getDateOfBirth()
                ))
                .orElse(null);
    }

    public UserProfileResponse saveOrUpdateProfile(Long userId, UserProfileRequest req) {

        UserProfile profile = profileRepo.findByUserId(userId)
                .orElse(UserProfile.builder()
                        .userId(userId)
                        .createdAt(LocalDateTime.now())
                        .build()
                );

        profile.setFullName(req.getFullName());
        profile.setGender(req.getGender());
        profile.setAge(req.getAge());
        profile.setPhone(req.getPhone());
        profile.setDateOfBirth(req.getDateOfBirth());
        profile.setUpdatedAt(LocalDateTime.now());

        UserProfile saved = profileRepo.save(profile);

        return new UserProfileResponse(
                saved.getFullName(),
                saved.getGender(),
                saved.getAge(),
                saved.getPhone(),
                saved.getDateOfBirth()
        );
    }
}
