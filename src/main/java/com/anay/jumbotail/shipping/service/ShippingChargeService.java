package com.anay.jumbotail.shipping.service;

import com.anay.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.anay.jumbotail.shipping.dto.ShippingChargeRequest;
import com.anay.jumbotail.shipping.dto.ShippingChargeResponse;
import com.anay.jumbotail.shipping.exception.InvalidRequestException;
import com.anay.jumbotail.shipping.exception.ResourceNotFoundException;
import com.anay.jumbotail.shipping.model.Customer;
import com.anay.jumbotail.shipping.model.Product;
import com.anay.jumbotail.shipping.model.Seller;
import com.anay.jumbotail.shipping.model.Warehouse;
import com.anay.jumbotail.shipping.repository.CustomerRepository;
import com.anay.jumbotail.shipping.repository.ProductRepository;
import com.anay.jumbotail.shipping.repository.SellerRepository;
import com.anay.jumbotail.shipping.strategy.DeliverySpeed;
import com.anay.jumbotail.shipping.strategy.TransportMode;
import com.anay.jumbotail.shipping.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingChargeService {
    
    private final WarehouseService warehouseService;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    @Cacheable(value = "shippingCharge", key = "#warehouseId + '_' + #customerId + '_' + #deliverySpeed + '_' + (#productId != null ? #productId : 'default')")
    @Transactional(readOnly = true)
    public Double calculateShippingCharge(Long warehouseId, String customerId, String deliverySpeed, String productId) {
        log.info("Calculating shipping charge for warehouseId: {}, customerId: {}, deliverySpeed: {}, productId: {}", warehouseId, customerId, deliverySpeed, productId);
        DeliverySpeed speed;
        try {
            speed = DeliverySpeed.fromCode(deliverySpeed);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid delivery speed: " + deliverySpeed + ". Must be 'standard' or 'express'");
        }
        
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
        
        if (warehouse.getLocation() == null || warehouse.getLocation().getLat() == null ||
            warehouse.getLocation().getLng() == null) {
            throw new ResourceNotFoundException("Warehouse location not available for warehouseId: " + warehouseId);
        }
        Customer customer = customerRepository.findByCustomerIdAndActiveTrue(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        
        if (customer.getLocation() == null || customer.getLocation().getLat() == null ||
            customer.getLocation().getLng() == null) {
            throw new ResourceNotFoundException("Customer location not available for customerId: " + customerId);
        }
        
        // Calculate distance
        double distance = DistanceCalculator.calculateDistance(warehouse.getLocation(), customer.getLocation());
        
        // Get product weight if productId is provided
        double weight = 1.0; // Default weight in kg
        if (productId != null && !productId.trim().isEmpty()) {
            Product product = productRepository.findByProductIdAndActiveTrue(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
            
            if (product.getAttributes() != null && product.getAttributes().getWeight() != null) {
                weight = product.getAttributes().getWeight();
            } else {
                log.warn("Product {} has no weight information, using default weight", productId);
            }
        } else {
            log.warn("ProductId not provided, using default weight {} kg for shipping charge calculation", weight);
        }
        
        return calculateShippingChargeInternal(distance, weight, speed);
    }
    
    @Transactional(readOnly = true)
    public ShippingChargeResponse calculateShippingChargeForSellerAndCustomer(ShippingChargeRequest request) {
        log.info("Calculating shipping charge for sellerId: {}, customerId: {}, deliverySpeed: {}", 
                 request.getSellerId(), request.getCustomerId(), request.getDeliverySpeed());
        
        // Validate delivery speed
        DeliverySpeed speed;
        try {
            speed = DeliverySpeed.fromCode(request.getDeliverySpeed());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid delivery speed: " + request.getDeliverySpeed() + 
                                             ". Must be 'standard' or 'express'");
        }
        
        // Get seller
        Seller seller = sellerRepository.findBySellerIdAndActiveTrue(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + request.getSellerId()));
        
        if (seller.getLocation() == null || seller.getLocation().getLat() == null ||
            seller.getLocation().getLng() == null) {
            throw new ResourceNotFoundException("Seller location not available for sellerId: " + request.getSellerId());
        }
        
        // Get customer
        Customer customer = customerRepository.findByCustomerIdAndActiveTrue(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));
        
        if (customer.getLocation() == null || customer.getLocation().getLat() == null ||
            customer.getLocation().getLng() == null) {
            throw new ResourceNotFoundException("Customer location not available for customerId: " + request.getCustomerId());
        }
        
        // Find nearest warehouse to seller
        // We need productId for this, but it's not in the request. Let's get seller's first product
        List<Product> sellerProducts = productRepository.findBySellerAndActiveTrue(seller);
        
        if (sellerProducts.isEmpty()) {
            throw new ResourceNotFoundException("No products found for sellerId: " + request.getSellerId());
        }
        
        // Use first product to find nearest warehouse
        Product product = sellerProducts.get(0);
        NearestWarehouseResponse nearestWarehouse = warehouseService.findNearestWarehouse(
                request.getSellerId(), product.getProductId());
        
        // Get warehouse entity
        Warehouse warehouse = warehouseService.getWarehouseById(nearestWarehouse.getWarehouseId());
        
        // Calculate distance from warehouse to customer
        double distance = DistanceCalculator.calculateDistance(warehouse.getLocation(), customer.getLocation());
        
        // Get product weight
        double weight = product.getAttributes() != null && product.getAttributes().getWeight() != null
                ? product.getAttributes().getWeight()
                : 1.0; // Default weight if not available
        
        // Calculate shipping charge
        Double shippingCharge = calculateShippingChargeInternal(distance, weight, speed);
        
        log.info("Shipping charge calculated: {} Rs for distance: {} km, weight: {} kg", 
                 shippingCharge, distance, weight);
        
        return ShippingChargeResponse.builder()
                .shippingCharge(shippingCharge)
                .nearestWarehouse(nearestWarehouse)
                .build();
    }
    private Double calculateShippingChargeInternal(double distance, double weight, DeliverySpeed speed) {
        // Determine transport mode based on distance
        TransportMode transportMode = TransportMode.getTransportMode(distance);
        
        log.debug("Transport mode: {} for distance: {} km", transportMode.getName(), distance);
        
        double baseCharge = transportMode.calculateCharge(distance, weight);
        double speedCharge = speed.calculateAdditionalCharge(weight);
        
        double totalCharge = baseCharge + speedCharge;
        
        log.debug("Base charge: {} Rs, Speed charge: {} Rs, Total: {} Rs", 
                  baseCharge, speedCharge, totalCharge);
        
        return Math.round(totalCharge * 100.0) / 100.0;
    }
}
