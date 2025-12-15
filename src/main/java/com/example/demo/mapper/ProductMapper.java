package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.entity.Product;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product p) {
        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl(),
                p.getCategory()
        );
    }

    public Product toEntity(ProductRequestDTO dto) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stock(dto.stock())
                .imageUrl(dto.imageUrl())
                .category(dto.category())
                .build();
    }

    public void updateEntity(Product p, ProductRequestDTO dto) {
        p.setName(dto.name());
        p.setDescription(dto.description());
        p.setPrice(dto.price());
        p.setStock(dto.stock());
        p.setImageUrl(dto.imageUrl());
        p.setCategory(dto.category());
    }
}
