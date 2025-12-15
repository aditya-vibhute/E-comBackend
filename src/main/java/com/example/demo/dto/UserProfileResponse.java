package com.example.demo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String fullName;
    private String gender;
    private Integer age;
    private String phone;
    private LocalDate dateOfBirth;
}
