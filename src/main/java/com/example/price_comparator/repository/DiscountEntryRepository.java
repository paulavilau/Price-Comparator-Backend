package com.example.price_comparator.repository;
import com.example.price_comparator.model.*;
import java.util.Optional;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountEntryRepository extends JpaRepository<DiscountEntry, Long> {
   Optional<DiscountEntry> findByProductAndStoreAndFromDateAndToDate(Product product, Store store, LocalDate fromDate, LocalDate toDate);
}