package com.anay.jumbotail.shipping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearestWarehouseResponse {
    private Long warehouseId;
    private LocationDTO warehouseLocation;
}
