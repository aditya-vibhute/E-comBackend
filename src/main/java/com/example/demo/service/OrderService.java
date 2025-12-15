package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.CheckoutInfoResponse;
import com.example.demo.dto.CheckoutRequest;
import com.example.demo.dto.OrderDetailsResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ShippingAddress;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class OrderService {

    private final CartItemRepository cartRepo;      // FIXED
    private final AddressRepository addressRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    
    @Autowired
    public ProductRepository productRepo;  // ADDED

    // FIXED CONSTRUCTOR → uses CartItemRepository instead of CartRepository
    public OrderService(CartItemRepository cartRepo,
                        AddressRepository addressRepo,
                        OrderRepository orderRepo,
                        OrderItemRepository orderItemRepo) {

        this.cartRepo = cartRepo;
        this.addressRepo = addressRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    // Get all orders of logged-in user
    public List<Order> getMyOrders(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public CheckoutInfoResponse getCheckoutInfo(Long userId) {

        // Fetch cart items of logged-in user
        List<CartItem> cartItems = cartRepo.findByUserId(userId);

        // Convert CartItem entities to CartItemDTO
        List<CartItemDTO> items = cartItems.stream()
                .map(c -> CartItemDTO.builder()
                        .productId(c.getProduct().getId())
                        .name(c.getProduct().getName())
                        .imageUrl(c.getProduct().getImageUrl())   // Product entity must have this
                        .price(c.getProduct().getPrice())
                        .quantity(c.getQuantity())
                        .build()
                )
                .toList();

        // Calculate total price
        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        // Load saved addresses
        List<ShippingAddress> addresses = addressRepo.findByUserId(userId);

        return new CheckoutInfoResponse(items, total, addresses);
    }




// Get order details + items for a single order
    public OrderDetailsResponse getOrderDetails(Long orderId, Long userId) {

        // Validate order belongs to this user
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId))
            throw new RuntimeException("Access denied for this order");

        // Fetch order items
        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);

        return new OrderDetailsResponse(order, items);
    }


    @Transactional
    public OrderResponse placeOrder(Long userId, CheckoutRequest req) {

        // 1. Fetch cart items
        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        if (cartItems.isEmpty())
            throw new RuntimeException("Cart is empty");

        // 2. Determine which address to use
        AddressDTO finalAddress;

        if (!req.isUseNewAddress()) {
            // Using saved address
            if (req.getAddressId() == null)
                throw new RuntimeException("AddressId is required");

            ShippingAddress saved = addressRepo
                    .findByIdAndUserId(req.getAddressId(), userId)
                    .orElseThrow(() -> new RuntimeException("Invalid address"));

            finalAddress = new AddressDTO(
                    saved.getFullName(),
                    saved.getPhone(),
                    saved.getStreet(),
                    saved.getCity(),
                    saved.getState(),
                    saved.getPincode()
            );

        } else {
            // Using new one-time address
            if (req.getNewAddress() == null)
                throw new RuntimeException("New address is required");

            finalAddress = req.getNewAddress();
        }

        // 3. Calculate total
        double total = cartItems.stream()
                .mapToDouble(i -> i.getQuantity() * i.getProduct().getPrice())
                .sum();

        // 4. Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        // Copy address snapshot
        order.setShippingName(finalAddress.getFullName());
        order.setShippingPhone(finalAddress.getPhone());
        order.setShippingStreet(finalAddress.getStreet());
        order.setShippingCity(finalAddress.getCity());
        order.setShippingState(finalAddress.getState());
        order.setShippingPincode(finalAddress.getPincode());

        orderRepo.save(order);

        // 5. Convert cart → order items
        for (CartItem c : cartItems) {

            Product product = c.getProduct();

            // 1. Validate stock
            if (product.getStock() < c.getQuantity()) {
                throw new RuntimeException(
                    "Insufficient stock for product: " + product.getName()
                );
            }

            // 🟢 2. Reduce stock
            product.setStock(product.getStock() - c.getQuantity());
            productRepo.save(product); // persist stock update

            // 🟢 3. Create order item
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setQuantity(c.getQuantity());
            item.setPrice(product.getPrice());

            orderItemRepo.save(item);
        }


        // 6. Clear cart
        cartRepo.deleteByUserId(userId);

        // 7. Return response
        return new OrderResponse(order.getId(), total, order.getStatus());
    }
}
