package com.example.price_comparator.model;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    @ManyToOne
    private Product product;

    private double targetPrice;

    private boolean triggered = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // I manually defined this constructor so that I dont have to pass  triggered and createdAt when I create a new alert
    // Because they have default valuea and are always the same at the time of creation
    public PriceAlert(String userEmail, Product product, double targetPrice) {
        this.userEmail = userEmail;
        this.product = product;
        this.targetPrice = targetPrice;
    }
}
