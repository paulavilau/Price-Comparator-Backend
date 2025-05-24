package com.example.price_comparator.controller;
import com.example.price_comparator.dto.basket.*;
import com.example.price_comparator.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    // Endpoint for optimizing a basket of products
    // Accepts a list of product IDs and a date
    // Returns the cheapest stores
    @PostMapping("/optimize")
    public BasketOptimizeResponse optimizeBasket(@RequestBody BasketOptimizeRequest request) {
        return basketService.optimizeBasket(request);
    }

}
