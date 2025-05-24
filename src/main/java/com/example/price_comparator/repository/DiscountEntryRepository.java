package com.example.price_comparator.repository;
import com.example.price_comparator.model.*;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// This interface defines custom queries and inherits basic CRUD methods for the DiscountEntry entity
public interface DiscountEntryRepository extends JpaRepository<DiscountEntry, Long> {

   // Finds a discount entry for a specific product, store, and date range
   Optional<DiscountEntry> findByProductAndStoreAndFromDateAndToDate(Product product, Store store, LocalDate fromDate, LocalDate toDate);

    // Returns all currently active discounts (i.e., where today is within the discount date range),
    // ordered by the highest discount percentage
   @Query("SELECT d FROM DiscountEntry d " +
         "WHERE :today BETWEEN d.fromDate AND d.toDate " +
         "ORDER BY d.percentageOfDiscount DESC")
   List<DiscountEntry> findActiveDiscountsOrderByPercentage(@Param("today") LocalDate today);

    // Retrieves discount entries that were added or imported in the last 24 hours   
    @Query("SELECT d FROM DiscountEntry d WHERE d.importDate >= :yesterday")
    List<DiscountEntry> findNewDiscounts(@Param("yesterday") LocalDate yesterday);
}