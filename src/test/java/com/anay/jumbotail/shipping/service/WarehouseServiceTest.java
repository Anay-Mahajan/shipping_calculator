package com.anay.jumbotail.shipping.service;

import com.anay.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.anay.jumbotail.shipping.exception.ResourceNotFoundException;
import com.anay.jumbotail.shipping.model.*;
import com.anay.jumbotail.shipping.repository.ProductRepository;
import com.anay.jumbotail.shipping.repository.SellerRepository;
import com.anay.jumbotail.shipping.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WarehouseServiceTest {
    
    @Autowired
    private WarehouseService warehouseService;
    
    @Autowired
    private SellerRepository sellerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    private Seller testSeller;
    private Product testProduct;
    private Warehouse warehouse1;
    private Warehouse warehouse2;
    
    @BeforeEach
    void setUp() {
        // Create test seller
        testSeller = Seller.builder()
                .sellerId("TEST-SELLER-001")
                .name("Test Seller")
                .location(new Location(12.5, 37.5))
                .build();
        testSeller = sellerRepository.save(testSeller);
        
        // Create test product
        testProduct = Product.builder()
                .productId("TEST-PRODUCT-001")
                .name("Test Product")
                .sellingPrice(100.0)
                .seller(testSeller)
                .attributes(new ProductAttributes(1.0, 10.0, 10.0, 10.0))
                .build();
        testProduct = productRepository.save(testProduct);
        
        warehouse1 = Warehouse.builder()
                .warehouseId("WH-001")
                .name("Warehouse 1")
                .location(new Location(12.6, 37.6)) // Closer to seller
                .build();
        warehouse1 = warehouseRepository.save(warehouse1);
        
        warehouse2 = Warehouse.builder()
                .warehouseId("WH-002")
                .name("Warehouse 2")
                .location(new Location(15.0, 40.0)) // Farther from seller
                .build();
        warehouse2 = warehouseRepository.save(warehouse2);
    }
    
    @Test
    void testFindNearestWarehouse_Success() {
        NearestWarehouseResponse response = warehouseService.findNearestWarehouse(
                testSeller.getSellerId(), testProduct.getProductId());
        assertNotNull(response);
        assertNotNull(response.getWarehouseId());
        assertNotNull(response.getWarehouseLocation());
        assertEquals(warehouse1.getId(), response.getWarehouseId());
    }
    
    @Test
    void testFindNearestWarehouse_SellerNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            warehouseService.findNearestWarehouse("NON-EXISTENT", testProduct.getProductId());
        });
    }
    
    @Test
    void testFindNearestWarehouse_ProductNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            warehouseService.findNearestWarehouse(testSeller.getSellerId(), "NON-EXISTENT");
        });
    }
}
