package com.anay.jumbotail.shipping.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributes {
    private Double weight; 
    private Double length; 
    private Double width;  
    private Double height; 
    public Double getVolume() {
        if (length == null || width == null || height == null) {
            return 0.0;
        }
        return length * width * height;
    }
}
