package com.example.price_comparator.dto.basket;
import java.util.List;

public record BasketOptimizeResponse(
    List<OptimizedProductDto> products,
    double totalEstimatedCost
) {}


