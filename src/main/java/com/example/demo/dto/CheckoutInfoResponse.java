package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.ShippingAddress;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutInfoResponse {
    private List<CartItemDTO> items;
    private double total;
    private List<ShippingAddress> addresses;
}
