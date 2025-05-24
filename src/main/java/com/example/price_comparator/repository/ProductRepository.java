package com.example.price_comparator.repository;
import com.example.price_comparator.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByProductNameContainingIgnoreCase(String namePart);

}

