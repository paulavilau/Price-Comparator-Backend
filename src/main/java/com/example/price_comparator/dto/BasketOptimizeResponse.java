package com.example.price_comparator.dto;

public record BasketOptimizeResponse(
    String productId,
    String productName,
    String bestStore,
    double price,
    String currency
) {}

