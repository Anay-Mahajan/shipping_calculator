package com.anay.jumbotail.shipping.strategy;
public enum DeliverySpeed {
    STANDARD("standard", 10.0, 0.0), 
    EXPRESS("express", 10.0, 1.2);   
    
    private final String code;
    private final double standardCharge; 
    private final double extraPerKg;    
    
    DeliverySpeed(String code, double standardCharge, double extraPerKg) {
        this.code = code;
        this.standardCharge = standardCharge;
        this.extraPerKg = extraPerKg;
    }
    public static DeliverySpeed fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Delivery speed code cannot be null");
        }
        
        String normalizedCode = code.toLowerCase().trim();
        for (DeliverySpeed speed : values()) {
            if (speed.code.equals(normalizedCode)) {
                return speed;
            }
        }
        
        throw new IllegalArgumentException("Invalid delivery speed: " + code + ". Must be 'standard' or 'express'");
    }
    public double calculateAdditionalCharge(double weight) {
        return standardCharge + (extraPerKg * weight);
    }
    
    public String getCode() {
        return code;
    }
    
    public double getStandardCharge() {
        return standardCharge;
    }
    
    public double getExtraPerKg() {
        return extraPerKg;
    }
}
