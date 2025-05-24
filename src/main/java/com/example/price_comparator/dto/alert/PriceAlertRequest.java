package com.example.price_comparator.dto.alert;

// Holds the info needed to create a new price alert
public record PriceAlertRequest(
    String userEmail,
    String productId,
    double targetPrice
) {}