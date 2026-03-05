package com.anay.jumbotail.shipping.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product entity representing products sold by sellers
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String productId; // e.g., "456"
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Double sellingPrice;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;
    
    @Embedded
    private ProductAttributes attributes;
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
