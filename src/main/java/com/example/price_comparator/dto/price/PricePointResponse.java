package com.example.price_comparator.dto.price;
import java.time.LocalDate;

public record PricePointResponse(
    LocalDate date,
    double price,
    String currency,
    String store
) {}

