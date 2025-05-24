package com.example.price_comparator.dto.price;

public record PriceHistoryFilterRequest(
    String productId,         
    String store                        
) {}
