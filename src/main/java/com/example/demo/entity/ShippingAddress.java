package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipping_addresses")
public class ShippingAddress {
    @Id @GeneratedValue
    private Long id;

    private Long userId;

    private String fullName;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String pincode;
}
