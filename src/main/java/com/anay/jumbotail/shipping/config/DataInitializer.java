package com.anay.jumbotail.shipping.config;

import com.anay.jumbotail.shipping.model.*;
import com.anay.jumbotail.shipping.repository.CustomerRepository;
import com.anay.jumbotail.shipping.repository.ProductRepository;
import com.anay.jumbotail.shipping.repository.SellerRepository;
import com.anay.jumbotail.shipping.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    
    @Override
    public void run(String... args) {
        log.info("Initializing sample data...");
        
        // Initialize Customers
        initializeCustomers();
        
        // Initialize Warehouses
        initializeWarehouses();
        
        // Initialize Sellers and Products
        initializeSellersAndProducts();
        
        log.info("Sample data initialization completed");
    }
    
    private void initializeCustomers() {
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .customerId("Cust-123")
                    .name("Shree Kirana Store")
                    .phoneNumber("9847123456")
                    .location(new Location(11.232, 23.445495))
                    .active(true)
                    .build();
            
            Customer customer2 = Customer.builder()
                    .customerId("Cust-124")
                    .name("Andheri Mini Mart")
                    .phoneNumber("9101234567")
                    .location(new Location(17.232, 33.445495))
                    .active(true)
                    .build();
            
            customerRepository.save(customer1);
            customerRepository.save(customer2);
            log.info("Initialized {} customers", customerRepository.count());
        }
    }
    
    private void initializeWarehouses() {
        if (warehouseRepository.count() == 0) {
            Warehouse warehouse1 = Warehouse.builder()
                    .warehouseId("789")
                    .name("BLR_Warehouse")
                    .location(new Location(12.99999, 37.923273))
                    .active(true)
                    .build();
            
            Warehouse warehouse2 = Warehouse.builder()
                    .warehouseId("790")
                    .name("MUMB_Warehouse")
                    .location(new Location(11.99999, 27.923273))
                    .active(true)
                    .build();
            
            warehouseRepository.save(warehouse1);
            warehouseRepository.save(warehouse2);
            log.info("Initialized {} warehouses", warehouseRepository.count());
        }
    }
    
    private void initializeSellersAndProducts() {
        if (sellerRepository.count() == 0) {
            // Nestle Seller
            Seller nestleSeller = Seller.builder()
                    .sellerId("123")
                    .name("Nestle Seller")
                    .location(new Location(12.5, 37.5)) // Near BLR warehouse
                    .active(true)
                    .build();
            nestleSeller = sellerRepository.save(nestleSeller);
            
            Product maggie = Product.builder()
                    .productId("456")
                    .name("Maggie 500g Packet")
                    .sellingPrice(10.0)
                    .seller(nestleSeller)
                    .attributes(new ProductAttributes(0.5, 10.0, 10.0, 10.0))
                    .active(true)
                    .build();
            productRepository.save(maggie);
            
            // Rice Seller
            Seller riceSeller = Seller.builder()
                    .sellerId("124")
                    .name("Rice Seller")
                    .location(new Location(11.5, 27.5)) // Near MUMB warehouse
                    .active(true)
                    .build();
            riceSeller = sellerRepository.save(riceSeller);
            
            Product riceBag = Product.builder()
                    .productId("457")
                    .name("Rice Bag 10Kg")
                    .sellingPrice(500.0)
                    .seller(riceSeller)
                    .attributes(new ProductAttributes(10.0, 1000.0, 800.0, 500.0))
                    .active(true)
                    .build();
            productRepository.save(riceBag);
            
            // Sugar Seller
            Seller sugarSeller = Seller.builder()
                    .sellerId("125")
                    .name("Sugar Seller")
                    .location(new Location(13.0, 38.0)) // Near BLR warehouse
                    .active(true)
                    .build();
            sugarSeller = sellerRepository.save(sugarSeller);
            
            Product sugarBag = Product.builder()
                    .productId("458")
                    .name("Sugar Bag 25kg")
                    .sellingPrice(700.0)
                    .seller(sugarSeller)
                    .attributes(new ProductAttributes(25.0, 1000.0, 900.0, 600.0))
                    .active(true)
                    .build();
            productRepository.save(sugarBag);
            
            log.info("Initialized {} sellers and {} products", 
                     sellerRepository.count(), productRepository.count());
        }
    }
}
