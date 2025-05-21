package com.example.price_comparator.repository;
import com.example.price_comparator.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {}

