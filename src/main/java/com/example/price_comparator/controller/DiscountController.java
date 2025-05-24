package com.example.price_comparator.controller;
import com.example.price_comparator.dto.discount.DiscountProductResponse;
import com.example.price_comparator.model.DiscountEntry;
import com.example.price_comparator.service.DiscountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // Endpoint to get the best discounts for today
    @GetMapping("/best")
    public List<DiscountProductResponse> getBestDiscounts() {
        return discountService.getBestDiscounts(LocalDate.now());
    }

    // Endpoint to get the best discounts for a specific date
    @GetMapping("/new")
    public List<DiscountEntry> getNewDiscounts() {
        return discountService.getNewDiscounts();
    }
}
