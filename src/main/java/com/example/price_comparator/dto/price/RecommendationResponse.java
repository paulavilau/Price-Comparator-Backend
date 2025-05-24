package com.example.price_comparator.dto.price;
import java.time.LocalDate;

public record RecommendationResponse(
    String productId,
    String productName,
    String brand,
    double price,
    String unit,
    double quantity,
    double valuePerUnit,
    String store,
    LocalDate date
) {}
