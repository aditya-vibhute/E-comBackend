package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryIgnoreCaseAndNameContainingIgnoreCase(String category, String name);
}
