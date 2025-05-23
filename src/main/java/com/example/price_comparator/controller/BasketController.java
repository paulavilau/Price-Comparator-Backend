package com.example.price_comparator.controller;
import com.example.price_comparator.dto.*;
import com.example.price_comparator.service.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping("/optimize")
    public List<BasketOptimizeResponse> optimizeBasket(@RequestBody BasketOptimizeRequest request) {
        return basketService.optimizeBasket(request);
    }
}
