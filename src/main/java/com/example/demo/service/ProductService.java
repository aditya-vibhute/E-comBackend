package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final ProductMapper mapper;

    public List<ProductDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public ProductDTO getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapper.toDTO(p);
    }

    public List<ProductDTO> getByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<ProductDTO> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<ProductDTO> filter(String category, String name) {
        return repo.findByCategoryIgnoreCaseAndNameContainingIgnoreCase(category, name)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public ProductDTO create(ProductRequestDTO req) {
        Product p = mapper.toEntity(req);
        repo.save(p);
        return mapper.toDTO(p);
    }

    public ProductDTO update(Long id, ProductRequestDTO req) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        mapper.updateEntity(p, req);
        repo.save(p);

        return mapper.toDTO(p);
    }

    public List<String> getAllCategories() {
        return repo.findDistinctCategories();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
