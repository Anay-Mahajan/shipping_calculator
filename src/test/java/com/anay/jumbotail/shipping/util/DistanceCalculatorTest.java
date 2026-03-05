package com.anay.jumbotail.shipping.util;

import com.anay.jumbotail.shipping.model.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorTest {
    
    @Test
    void testCalculateDistance_SameLocation() {
        Location loc1 = new Location(12.9716, 77.5946); // Bangalore
        Location loc2 = new Location(12.9716, 77.5946);
        
        double distance = DistanceCalculator.calculateDistance(loc1, loc2);
        
        assertEquals(0.0, distance, 0.01);
    }
    
    @Test
    void testCalculateDistance_DifferentLocations() {
        Location loc1 = new Location(12.9716, 77.5946); // Bangalore
        Location loc2 = new Location(19.0760, 72.8777); // Mumbai
        
        double distance = DistanceCalculator.calculateDistance(loc1, loc2);
        
        assertTrue(distance > 0);
        assertTrue(distance < 1000); // Should be less than 1000 km
    }
    
    @Test
    void testCalculateDistance_NullLocation() {
        Location loc1 = new Location(12.9716, 77.5946);
        
        assertThrows(IllegalArgumentException.class, () -> {
            DistanceCalculator.calculateDistance(loc1, null);
        });
    }
}
