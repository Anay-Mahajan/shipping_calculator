package com.anay.jumbotail.shipping.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingChargeRequest {
    
    @NotBlank(message = "Seller ID is required")
    private String sellerId;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Delivery speed is required")
    private String deliverySpeed; 
}
