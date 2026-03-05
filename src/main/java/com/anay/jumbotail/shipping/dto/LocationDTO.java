package com.anay.jumbotail.shipping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Double lat;
    private Double lng;
}
