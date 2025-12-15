package com.example.demo.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ProfilePageResponse {
    private UserProfileResponse profile;
    private List<AddressResponse> addresses;
}

