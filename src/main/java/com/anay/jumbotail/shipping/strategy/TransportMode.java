package com.anay.jumbotail.shipping.strategy;

/**
 * Enum representing different transport modes
 */
public enum TransportMode {
    MINI_VAN("Mini Van", 0, 100, 3.0),
    TRUCK("Truck", 100, 500, 2.0),
    AEROPLANE("Aeroplane", 500, Double.MAX_VALUE, 1.0);
    
    private final String name;
    private final double minDistance;
    private final double maxDistance;
    private final double ratePerKmPerKg; // Rate in Rs per km per kg
    
    TransportMode(String name, double minDistance, double maxDistance, double ratePerKmPerKg) {
        this.name = name;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.ratePerKmPerKg = ratePerKmPerKg;
    }
    
    /**
     * Determine transport mode based on distance
     * 
     * @param distance Distance in kilometers
     * @return Appropriate TransportMode
     */
    public static TransportMode getTransportMode(double distance) {
        if (distance < MINI_VAN.maxDistance) {
            return MINI_VAN;
        } else if (distance < TRUCK.maxDistance) {
            return TRUCK;
        } else {
            return AEROPLANE;
        }
    }
    
    /**
     * Calculate shipping charge based on distance and weight
     * 
     * @param distance Distance in kilometers
     * @param weight Weight in kilograms
     * @return Shipping charge in rupees
     */
    public double calculateCharge(double distance, double weight) {
        return distance * weight * ratePerKmPerKg;
    }
    
    public String getName() {
        return name;
    }
    
    public double getMinDistance() {
        return minDistance;
    }
    
    public double getMaxDistance() {
        return maxDistance;
    }
    
    public double getRatePerKmPerKg() {
        return ratePerKmPerKg;
    }
}
