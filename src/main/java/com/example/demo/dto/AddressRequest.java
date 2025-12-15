package com.example.demo.dto;

import lombok.Data;

@Data
public class AddressRequest {
    private String fullName;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String pincode;
}
