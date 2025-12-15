package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CheckoutInfoResponse;
import com.example.demo.dto.CheckoutRequest;
import com.example.demo.dto.OrderDetailsResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.Order;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtService;

    public OrderController(OrderService orderService, JwtUtil jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @GetMapping("/checkout-info")
    public ResponseEntity<CheckoutInfoResponse> getCheckoutInfo(
            @RequestHeader("Authorization") String auth) {

        String token = auth.substring(7);
        Long userId = jwtService.extractUserId(token);

        CheckoutInfoResponse response = orderService.getCheckoutInfo(userId);

        return ResponseEntity.ok(response);
    }


    // =======================
    //       CHECKOUT
    // =======================
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @RequestBody CheckoutRequest req,
            @RequestHeader("Authorization") String auth) {

        String token = auth.substring(7);
        Long userId = jwtService.extractUserId(token);

        OrderResponse response = orderService.placeOrder(userId, req);
        return ResponseEntity.ok(response);
    }

    // =======================
    //     MY ORDERS
    // =======================
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(
            @RequestHeader("Authorization") String auth) {

        String token = auth.substring(7);
        Long userId = jwtService.extractUserId(token);

        return ResponseEntity.ok(orderService.getMyOrders(userId));
    }

    // =======================
    //   ORDER DETAILS
    // =======================
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {

        String token = auth.substring(7);
        Long userId = jwtService.extractUserId(token);

        return ResponseEntity.ok(orderService.getOrderDetails(id, userId));
    }
}
