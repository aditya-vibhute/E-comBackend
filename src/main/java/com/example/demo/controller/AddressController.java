package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.AddressService;

@RestController
@RequestMapping("/api/profile/addresses")
public class AddressController {

    private final AddressService addressService;
    private final JwtUtil jwtService;

    public AddressController(AddressService addressService,
                             JwtUtil jwtService) {
        this.addressService = addressService;
        this.jwtService = jwtService;
    }

    // 🔹 Get all saved addresses
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses(
            @RequestHeader("Authorization") String auth) {

        Long userId = jwtService.extractUserId(auth.substring(7));
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    // 🔹 Add new address
    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @RequestHeader("Authorization") String auth,
            @RequestBody AddressRequest req) {

        Long userId = jwtService.extractUserId(auth.substring(7));
        return ResponseEntity.ok(addressService.addAddress(userId, req));
    }

    // 🔹 Delete address
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id) {

        Long userId = jwtService.extractUserId(auth.substring(7));
        addressService.deleteAddress(userId, id);
        return ResponseEntity.noContent().build();
    }
}
