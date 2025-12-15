package com.example.demo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public Object getCart(@RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.extractUserId(auth.substring(7));
        return cartService.getUserCart(userId);
    }

    @PostMapping("/add")
    public String addToCart(@RequestHeader("Authorization") String auth,
                            @RequestBody AddToCartRequest req) {
        Long userId = jwtUtil.extractUserId(auth.substring(7));
        cartService.addToCart(userId, req);
        return "Item added to cart";
    }

    @PutMapping("/{productId}")
    public String updateQuantity(@RequestHeader("Authorization") String auth,
                                 @PathVariable Long productId,
                                 @RequestParam int quantity) {
        Long userId = jwtUtil.extractUserId(auth.substring(7));
        cartService.updateQuantity(userId, productId, quantity);
        return "Cart updated";
    }

    @DeleteMapping("/{productId}")
    public String remove(@RequestHeader("Authorization") String auth,
                         @PathVariable Long productId) {
        Long userId = jwtUtil.extractUserId(auth.substring(7));
        cartService.removeFromCart(userId, productId);
        return "Item removed";
    }

    @DeleteMapping("/clear")
    public String clearCart(@RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.extractUserId(auth.substring(7));
        cartService.clearCart(userId);
        return "Cart cleared";
    }
}
