package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    // USER: Get all products
    @GetMapping
    public List<ProductDTO> getAll() {
        return service.getAll();
    }

    // USER: Search by name
    @GetMapping("/search")
    public List<ProductDTO> search(@RequestParam String name) {
        return service.searchByName(name);
    }

    // USER: Filter by category
    @GetMapping("/category/{category}")
    public List<ProductDTO> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }

    // USER: Combined filter
    @GetMapping("/filter")
    public List<ProductDTO> filter(@RequestParam(required = false) String category,
                                   @RequestParam(required = false) String name) {
        return service.filter(category, name);
    }

    // ADMIN: create
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO create(@RequestBody ProductRequestDTO req) {
        return service.create(req);
    }

    // ADMIN: update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductRequestDTO req) {
        return service.update(id, req);
    }

    // ADMIN: delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
