package com.anay.jumbotail.shipping.service;

import com.anay.jumbotail.shipping.dto.LocationDTO;
import com.anay.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.anay.jumbotail.shipping.exception.ResourceNotFoundException;
import com.anay.jumbotail.shipping.model.Location;
import com.anay.jumbotail.shipping.model.Seller;
import com.anay.jumbotail.shipping.model.Warehouse;
import com.anay.jumbotail.shipping.repository.ProductRepository;
import com.anay.jumbotail.shipping.repository.SellerRepository;
import com.anay.jumbotail.shipping.repository.WarehouseRepository;
import com.anay.jumbotail.shipping.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public NearestWarehouseResponse findNearestWarehouse(String sellerId, String productId) {
        log.info("Finding nearest warehouse for sellerId: {}, productId: {}", sellerId, productId);
        Seller seller = sellerRepository.findBySellerIdAndActiveTrue(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));
       productRepository.findByProductIdAndActiveTrue(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        if (seller.getLocation() == null || seller.getLocation().getLat() == null || 
            seller.getLocation().getLng() == null) {
            throw new ResourceNotFoundException("Seller location not available for sellerId: " + sellerId);
        }
        List<Warehouse> warehouses = warehouseRepository.findByActiveTrue();
        if (warehouses.isEmpty()) {
            throw new ResourceNotFoundException("No active warehouses found in the system");
        }
        Warehouse nearestWarehouse = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getLocation() == null || warehouse.getLocation().getLat() == null ||
                warehouse.getLocation().getLng() == null) {
                log.warn("Warehouse {} has invalid location, skipping", warehouse.getWarehouseId());
                continue;
            }   
            double distance = DistanceCalculator.calculateDistance(seller.getLocation(), warehouse.getLocation());
            
            if (distance < minDistance) {
                minDistance = distance;
                nearestWarehouse = warehouse;
            }
        }
        
        if (nearestWarehouse == null) {
            throw new ResourceNotFoundException("No warehouse with valid location found");
        }
        
        log.info("Nearest warehouse found: {} at distance: {} km", 
                 nearestWarehouse.getWarehouseId(), minDistance);
        
        Location warehouseLocation = nearestWarehouse.getLocation();
        LocationDTO locationDTO = new LocationDTO(warehouseLocation.getLat(), warehouseLocation.getLng());
        
        return NearestWarehouseResponse.builder().warehouseId(nearestWarehouse.getId()).warehouseLocation(locationDTO).build();
    }
    @Transactional(readOnly = true)
    public Warehouse getWarehouseById(Long warehouseId) {
        return warehouseRepository.findById(warehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + warehouseId));
    }
    @Transactional(readOnly = true)
    public Warehouse getWarehouseByWarehouseId(String warehouseId) {
        return warehouseRepository.findByWarehouseIdAndActiveTrue(warehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + warehouseId));
    }
}
