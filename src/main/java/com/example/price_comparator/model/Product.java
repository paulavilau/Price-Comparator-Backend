package com.example.price_comparator.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String productId;

    private String productName;

    private String productCategory;

    private String brand;

    private double packageQuantity;
    
    private String packageUnit;
}
