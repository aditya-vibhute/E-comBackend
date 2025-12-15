package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    private Long id;

    private Long userId;
    private Double totalAmount;

    private String status;   // CREATED, PAID, SHIPPED, DELIVERED, CANCELLED

    // Address snapshot
    private String shippingName;
    private String shippingPhone;
    private String shippingStreet;
    private String shippingCity;
    private String shippingState;
    private String shippingPincode;

    private LocalDateTime createdAt;
}

