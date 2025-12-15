package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id @GeneratedValue
    private Long id;

    private Long orderId;
    private Long productId;

    private String productName;
    private Integer quantity;

    private Double price;  // Price at time of purchase
}

