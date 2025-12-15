package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ShippingAddress;

@Repository
public interface AddressRepository extends JpaRepository<ShippingAddress, Long> {

    // Fetch all addresses of the current user
    List<ShippingAddress> findByUserId(Long userId);

    // Ensure the address belongs to the current user
    Optional<ShippingAddress> findByIdAndUserId(Long id, Long userId);
}

