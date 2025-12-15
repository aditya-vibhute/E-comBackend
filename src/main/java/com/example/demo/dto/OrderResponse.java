package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderResponse {
    private Long orderId;
    private Double totalAmount;
    private String status;

    public OrderResponse(Long id, Double amt, String status) {
        this.orderId = id;
        this.totalAmount = amt;
        this.status = status;
    }
}
