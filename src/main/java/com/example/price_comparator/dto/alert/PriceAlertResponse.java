package com.example.price_comparator.dto.alert;

public record PriceAlertResponse(
    String productId,
    String productName,
    String store,
    double currentPrice,
    double targetPrice,
    String currency,
    String userEmail
) {}
