package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {
    private Long productId;
    private String name;
    private String imageUrl;
    private double price;
    private int quantity;
}
