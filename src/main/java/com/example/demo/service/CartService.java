package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;

    public List<CartItemDTO> getUserCart(Long userId) {
        return cartRepo.findByUserId(userId)
                .stream()
                .map(item -> CartItemDTO.builder()
                        .productId(item.getProduct().getId())
                        .name(item.getProduct().getName())
                        .imageUrl(item.getProduct().getImageUrl())
                        .price(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .build()
                )
                .toList();
    }

    public void addToCart(Long userId, AddToCartRequest req) {
        Product product = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (req.getQuantity() <= 0) {
            throw new RuntimeException("Invalid quantity");
        }

        if (req.getQuantity() > product.getStock()) {
            throw new RuntimeException("Not enough stock available");
        }

        // Check if item already exists in cart
        CartItem cartItem = cartRepo.findByUserIdAndProduct_Id(userId, req.getProductId())
                .orElse(null);

        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + req.getQuantity();
            if (newQuantity > product.getStock()) {
                throw new RuntimeException("Exceeds available stock");
            }
            cartItem.setQuantity(newQuantity);
        } else {
            cartItem = CartItem.builder()
                    .product(product)
                    .userId(userId)
                    .quantity(req.getQuantity())
                    .build();
        }

        cartRepo.save(cartItem);
    }

    public void updateQuantity(Long userId, Long productId, int quantity) {
        CartItem item = cartRepo.findByUserIdAndProduct_Id(userId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (quantity <= 0) {
            cartRepo.delete(item);
            return;
        }

        if (quantity > item.getProduct().getStock()) {
            throw new RuntimeException("Not enough stock");
        }

        item.setQuantity(quantity);
        cartRepo.save(item);
    }

    public void removeFromCart(Long userId, Long productId) {
        CartItem item = cartRepo.findByUserIdAndProduct_Id(userId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        cartRepo.delete(item);
    }

    public void clearCart(Long userId) {
        cartRepo.deleteByUserId(userId);
    }
}
