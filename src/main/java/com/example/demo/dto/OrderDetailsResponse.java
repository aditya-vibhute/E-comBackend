package com.example.demo.dto;


import java.util.List;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailsResponse {

    private Order order;
    private List<OrderItem> items;
}
