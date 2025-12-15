package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.entity.ShippingAddress;
import com.example.demo.repository.AddressRepository;

@Service
public class AddressService {

    private final AddressRepository addressRepo;

    public AddressService(AddressRepository addressRepo) {
        this.addressRepo = addressRepo;
    }

    public List<AddressResponse> getUserAddresses(Long userId) {
        return addressRepo.findByUserId(userId).stream()
                .map(a -> new AddressResponse(
                        a.getId(),
                        a.getFullName(),
                        a.getPhone(),
                        a.getStreet(),
                        a.getCity(),
                        a.getState(),
                        a.getPincode()
                ))
                .toList();
    }

    public AddressResponse addAddress(Long userId, AddressRequest req) {

        ShippingAddress address = ShippingAddress.builder()
                .userId(userId)
                .fullName(req.getFullName())
                .phone(req.getPhone())
                .street(req.getStreet())
                .city(req.getCity())
                .state(req.getState())
                .pincode(req.getPincode())
                .build();

        ShippingAddress saved = addressRepo.save(address);

        return new AddressResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getPhone(),
                saved.getStreet(),
                saved.getCity(),
                saved.getState(),
                saved.getPincode()
        );
    }

    public void deleteAddress(Long userId, Long addressId) {
        ShippingAddress address = addressRepo
                .findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addressRepo.delete(address);
    }
}
