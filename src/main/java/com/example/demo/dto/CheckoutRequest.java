package com.example.demo.dto;

import lombok.Data;

@Data
public class CheckoutRequest {

    private Long addressId;            // optional
    private boolean useNewAddress;     // true => use custom address

    private AddressDTO newAddress;     // only used when useNewAddress=true
}
