package com.anay.jumbotail.shipping.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String warehouseId; 
    @Column(nullable = false, unique = true)
    private String name; 
    @Embedded
    private Location location;
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
