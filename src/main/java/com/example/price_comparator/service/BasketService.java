package com.example.price_comparator.service;
import com.example.price_comparator.model.*;
import com.example.price_comparator.dto.*;
import com.example.price_comparator.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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

    public List<BasketOptimizeResponse> optimizeBasket(BasketOptimizeRequest request) {
        List<BasketOptimizeResponse> result = new ArrayList<>();

        for (String productId : request.productIds()) {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) continue;

            Product product = productOpt.get();

            List<PriceEntry> entries = priceEntryRepository.findByProductAndDate(product, request.date());

            entries.stream()
                .min(Comparator.comparingDouble(PriceEntry::getPrice))
                .ifPresent(minEntry -> result.add(
                    new BasketOptimizeResponse(
                        product.getProductId(),
                        product.getProductName(),
                        minEntry.getStore().getName(),
                        minEntry.getPrice(),
                        minEntry.getCurrency()
                    )
                ));
        }

        return result;
    }
}

