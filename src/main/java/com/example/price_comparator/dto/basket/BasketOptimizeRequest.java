package com.example.price_comparator.dto.basket;
import java.time.LocalDate;
import java.util.List;

// Has the info  for the basket optimization request
// I used record classes because they are like simple final classes and they automatically generate setters and getters
public record BasketOptimizeRequest(
    LocalDate date,
    List<String> productIds
) {}

