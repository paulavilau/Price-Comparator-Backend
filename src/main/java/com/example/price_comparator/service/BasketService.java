package com.example.price_comparator.service;

import com.example.price_comparator.model.*;
import com.example.price_comparator.dto.basket.OptimizedProductDto;
import com.example.price_comparator.dto.basket.BasketOptimizeResponse;
import com.example.price_comparator.dto.basket.BasketOptimizeRequest;
import com.example.price_comparator.repository.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BasketService {

    private final PriceEntryRepository priceEntryRepository;
    private final ProductRepository productRepository;

    public BasketService(PriceEntryRepository priceEntryRepository, ProductRepository productRepository) {
        this.priceEntryRepository = priceEntryRepository;
        this.productRepository = productRepository;
    }

     // Optimizes the shopping basket by finding the lowest price for each product on the specified date
     // Returns a list of optimized products and the estimated total
    public BasketOptimizeResponse optimizeBasket(BasketOptimizeRequest request) {
        List<OptimizedProductDto> result = new ArrayList<>();
        double[] total = {0.0};

        for (String productId : request.productIds()) {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) continue; // If we can't find the product, we skip it

            Product product = productOpt.get();

            List<PriceEntry> entries = priceEntryRepository.findByProductAndDate(product, request.date());

            // If there are prices, we find the minimum one
            entries.stream()
                .min(Comparator.comparingDouble(PriceEntry::getPrice))
                .ifPresent(minEntry -> {
                    // We add the optimized product to the result list
                    result.add(new OptimizedProductDto(
                        product.getProductId(),
                        product.getProductName(),
                        minEntry.getStore().getName(),
                        minEntry.getPrice(),
                        minEntry.getCurrency()
                    ));

                    // We add the price to the total
                    total[0] += minEntry.getPrice();
                });
        }

        // We return the response with the optimized products and the total price
        return new BasketOptimizeResponse(result, total[0]);
    }
}
