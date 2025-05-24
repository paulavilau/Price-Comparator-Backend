package com.example.price_comparator.dto.discount;

// Has the info for the basket optimization request
// I used record classes because they are like simple final classes and they automatically generate setters and getters
public record DiscountProductResponse(
    String productId,
    String productName,
    String storeName,
    double discountPercentage,
    String category,
    String brand
) {}
