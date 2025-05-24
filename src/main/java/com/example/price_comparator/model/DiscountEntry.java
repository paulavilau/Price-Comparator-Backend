package com.example.price_comparator.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

// This class represents a discount entry for a product in a specific store

@Entity  // I use JPA to store this entity in a database
@Data   // Comes from Lombok, generates getters, setters etc.
@NoArgsConstructor   // Generates a constructor without parameters
@AllArgsConstructor  // Generates a constructor with all parameters
public class DiscountEntry {

    // Automatically generated IDs, with the auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The entity has a many-to-one relationship with Product and Store entities
    // The primary key of the target entity is used as the reference for the foreign key
    @ManyToOne
    private Product product;

    @ManyToOne
    private Store store;

    private LocalDate fromDate;

    private LocalDate toDate;

    private LocalDate importDate; 

    private double percentageOfDiscount;
}
