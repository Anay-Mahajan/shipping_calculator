package com.anay.jumbotail.shipping.controller;

import com.anay.jumbotail.shipping.dto.ShippingChargeRequest;
import com.anay.jumbotail.shipping.dto.ShippingChargeResponse;
import com.anay.jumbotail.shipping.service.ShippingChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/shipping-charge")
@RequiredArgsConstructor
@Slf4j
public class ShippingChargeController {
    
    private final ShippingChargeService shippingChargeService;
    @GetMapping
    public ResponseEntity<Map<String, Double>> getShippingCharge(@RequestParam Long warehouseId,@RequestParam String customerId,@RequestParam String deliverySpeed,@RequestParam(required = false) String productId) {
        
        log.info("Received request for shipping charge - warehouseId: {}, customerId: {}, deliverySpeed: {}, productId: {}",  warehouseId, customerId, deliverySpeed, productId);
        Double shippingCharge = shippingChargeService.calculateShippingCharge(warehouseId, customerId, deliverySpeed, productId);
        Map<String, Double> response = new HashMap<>();
        response.put("shippingCharge", shippingCharge);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/calculate")
    public ResponseEntity<ShippingChargeResponse> calculateShippingCharge(
            @Valid @RequestBody ShippingChargeRequest request) {
        
        log.info("Received request to calculate shipping charge - sellerId: {}, customerId: {}, deliverySpeed: {}", 
                 request.getSellerId(), request.getCustomerId(), request.getDeliverySpeed());
        
        ShippingChargeResponse response = shippingChargeService.calculateShippingChargeForSellerAndCustomer(request);
        
        return ResponseEntity.ok(response);
    }
}
