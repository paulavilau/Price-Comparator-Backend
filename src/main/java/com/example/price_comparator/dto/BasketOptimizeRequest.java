package com.example.price_comparator.dto;
import java.time.LocalDate;
import java.util.List;

public record BasketOptimizeRequest(
    LocalDate date,
    List<String> productIds
) {}

