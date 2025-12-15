package com.example.demo.dto;

public record ProductDTO(
    Long productId,
    String name,
    String description,
    Double price,
    Integer stock,
    String imageUrl,
    String category
) {
}
