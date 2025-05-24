package com.example.price_comparator.utils;

// Utility class for converting various units to base units (kilograms for weight, liters for volume)
public class UnitConverter {
    public static double toBaseUnit(double quantity, String unit) {
        return switch (unit.toLowerCase()) {
            case "g" -> quantity / 1000.0;     // grams to kgs
            case "ml" -> quantity / 1000.0;    // millilters to liters
            case "kg", "l", "buc", "role" -> quantity;  // aleady in base units
            default -> 1.0; // default case for unknown units, return 1.0
        };
    }
}
