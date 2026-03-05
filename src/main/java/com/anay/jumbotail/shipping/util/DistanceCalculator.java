package com.anay.jumbotail.shipping.util;

import com.anay.jumbotail.shipping.model.Location;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class DistanceCalculator {
    
    private static final double EARTH_RADIUS_KM = 6371.0;
    public static double calculateDistance(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }
        
        if (location1.getLat() == null || location1.getLng() == null ||
            location2.getLat() == null || location2.getLng() == null) {
            throw new IllegalArgumentException("Location coordinates cannot be null");
        }
        double lat1 = Math.toRadians(location1.getLat());
        double lon1 = Math.toRadians(location1.getLng());
        double lat2 = Math.toRadians(location2.getLat());
        double lon2 = Math.toRadians(location2.getLng());
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        double distance = EARTH_RADIUS_KM * c;
        
        log.debug("Distance between ({}, {}) and ({}, {}): {} km", 
                  location1.getLat(), location1.getLng(),
                  location2.getLat(), location2.getLng(), distance);
        
        return distance;
    }
}
