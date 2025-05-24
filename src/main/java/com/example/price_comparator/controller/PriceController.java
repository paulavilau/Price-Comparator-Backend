package com.example.price_comparator.controller;
import com.example.price_comparator.dto.*;
import com.example.price_comparator.dto.price.PriceHistoryFilterRequest;
import com.example.price_comparator.dto.price.PricePointResponse;
import com.example.price_comparator.dto.price.RecommendationResponse;
import com.example.price_comparator.service.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    // Returns the prices of a given product over time, optionally filtered by store
    @PostMapping("/history")
    public List<PricePointResponse> getPriceHistory(@RequestBody PriceHistoryFilterRequest request) {
        return priceService.getPriceHistory(request);
    }

    // Returns products with the best value per unit, for a given category and date     
    @GetMapping("/recommendations")
    public List<RecommendationResponse> getBestValueProducts(
            @RequestParam String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return priceService.getBestValueProducts(category, date);
    }

    // Returns all available options for a product name, from all stores
    // The options are ordered by best value per unit
    @GetMapping("/best-value")
    public List<RecommendationResponse> getBestValueForProduct(
            @RequestParam String productName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return priceService.getBestValueForProduct(productName, date);
    }
}
