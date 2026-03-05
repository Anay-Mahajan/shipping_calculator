package com.anay.jumbotail.shipping.controller;

import com.anay.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.anay.jumbotail.shipping.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Slf4j
public class WarehouseController {
    private final WarehouseService warehouseService;
    @GetMapping("/nearest")
    public ResponseEntity<NearestWarehouseResponse> getNearestWarehouse(@RequestParam String sellerId,@RequestParam String productId) {
        log.info("Received request for nearest warehouse - sellerId: {}, productId: {}", sellerId, productId);
        NearestWarehouseResponse response = warehouseService.findNearestWarehouse(sellerId, productId);
        return ResponseEntity.ok(response);
    }
}
