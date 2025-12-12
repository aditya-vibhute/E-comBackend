package com.example.demo.dto;

public record ProductRequestDTO(
        String name,
        String description,
        Double price,
        Integer stock,
        String imageUrl,
        String category
) {}
