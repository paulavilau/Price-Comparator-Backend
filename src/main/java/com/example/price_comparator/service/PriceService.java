package com.example.price_comparator.service;
import com.example.price_comparator.dto.price.PriceHistoryFilterRequest;
import com.example.price_comparator.dto.price.PricePointResponse;
import com.example.price_comparator.dto.price.RecommendationResponse;
import com.example.price_comparator.model.*;
import com.example.price_comparator.repository.*;
import com.example.price_comparator.utils.UnitConverter;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.util.Comparator;

@Service
public class PriceService {

    private final PriceEntryRepository priceEntryRepository;
    private final ProductRepository productRepository;

    // Constructor-based dependency injection
    public PriceService(PriceEntryRepository priceEntryRepository, ProductRepository productRepository, PriceAlertRepository alertRepository, JavaMailSender mailSender) {
        this.priceEntryRepository = priceEntryRepository;
        this.productRepository = productRepository;
    }

    // Returns the historical price data for a given product
    // This is for the "Dynamic Price History Graphs" feature
    public List<PricePointResponse> getPriceHistory(PriceHistoryFilterRequest request) {
    return productRepository.findById(request.productId())
        .map(product -> priceEntryRepository.findByProduct(product).stream()
            .filter(entry -> request.store() == null || entry.getStore().getName().equalsIgnoreCase(request.store()))
            .map(entry -> new PricePointResponse(
                entry.getDate(),
                entry.getPrice(),
                entry.getCurrency(),
                entry.getStore().getName()
            ))
            .sorted(Comparator.comparing(PricePointResponse::date))
            .toList())
        .orElse(List.of());
    }

    // Finds the best "value per unit" for a product name on a given date
    // This is useful when the same product exists in multiple package sizes
    public List<RecommendationResponse> getBestValueForProduct(String productName, LocalDate date) {
        List<Product> matches = productRepository.findByProductNameContainingIgnoreCase(productName);

        return matches.stream()
            .flatMap(p -> priceEntryRepository.findByProductAndDate(p, date).stream()
                .map(entry -> {
                    double qty = p.getPackageQuantity();
                    double baseQty = UnitConverter.toBaseUnit(qty, p.getPackageUnit());
                    double valuePerUnit = entry.getPrice() / baseQty;

                    return new RecommendationResponse(
                        p.getProductId(),
                        p.getProductName(),
                        p.getBrand(),
                        entry.getPrice(),
                        p.getPackageUnit(),
                        qty,
                        valuePerUnit,
                        entry.getStore().getName(),
                        entry.getDate()
                    );
                }))

            // Sort products by value per unit (ascending) to recommend the best deal
            .sorted(Comparator.comparingDouble(RecommendationResponse::valuePerUnit))
            .toList();
    }

    // Recommends the best products in a given category based on their unit value on a specific date
    // Useful for the "Product Substitutes & Recommendations" feature
    public List<RecommendationResponse> getBestValueProducts(String category, LocalDate date) {
        List<PriceEntry> entries = priceEntryRepository.findByDate(date);

        return entries.stream()
            .filter(e -> e.getProduct().getProductCategory().equalsIgnoreCase(category))
            .map(e -> {
                double quantity = e.getProduct().getPackageQuantity();
                String unit = e.getProduct().getPackageUnit();
                double baseQty = UnitConverter.toBaseUnit(quantity, unit);
                double valuePerUnit = e.getPrice() / baseQty;

                return new RecommendationResponse(
                    e.getProduct().getProductId(),
                    e.getProduct().getProductName(),
                    e.getProduct().getBrand(),
                    e.getPrice(),
                    unit,
                    quantity,
                    valuePerUnit,
                    e.getStore().getName(),
                    e.getDate()
                );
            })

            // Sort products by value per unit (ascending) to recommend the best deal
            .sorted(Comparator.comparingDouble(RecommendationResponse::valuePerUnit))
            .toList();
    }
}
