package com.example.price_comparator.dto.basket;

public record OptimizedProductDto(
    String productId,
    String productName,
    String bestStore,
    double price,
    String currency
) {}