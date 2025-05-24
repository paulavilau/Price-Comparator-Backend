package com.example.price_comparator.repository;
import com.example.price_comparator.model.PriceAlert;
import com.example.price_comparator.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByTriggeredFalse();

    boolean existsByUserEmailAndProductAndTargetPriceAndTriggeredFalse(
    String userEmail, Product product, double targetPrice
);

}