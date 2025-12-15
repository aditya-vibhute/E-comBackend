package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddressDTO {
    private String fullName;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String pincode;
}
