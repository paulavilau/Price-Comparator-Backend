package com.example.price_comparator.repository;
import com.example.price_comparator.model.*;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceEntryRepository extends JpaRepository<PriceEntry, Long> {
    List<PriceEntry> findByProduct(Product product);

    Optional<PriceEntry> findByProductAndStoreAndDate(Product product, Store store, LocalDate date);

    List<PriceEntry> findByProductAndDate(Product product, LocalDate date);

    List<PriceEntry> findByDate(LocalDate date);
}
