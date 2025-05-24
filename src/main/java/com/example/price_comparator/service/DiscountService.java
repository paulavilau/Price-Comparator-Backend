package com.example.price_comparator.service;
import com.example.price_comparator.dto.discount.DiscountProductResponse;
import com.example.price_comparator.model.DiscountEntry;
import com.example.price_comparator.repository.DiscountEntryRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountService {

    private final DiscountEntryRepository discountEntryRepository;

    public DiscountService(DiscountEntryRepository discountEntryRepository) {
        this.discountEntryRepository = discountEntryRepository;
    }

     // Returns all discounts that were added to the system in the last 24 hours
     // Its based on the importDate field which represents the date when the discount was importeD    
    public List<DiscountEntry> getNewDiscounts() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return discountEntryRepository.findNewDiscounts(yesterday);
    }

     // Returns the best active discounts for a specific day
    // It searches for discounts where the current date is between from_date and to_date
    // The result is ordered in descending order by the discount percentage
    public List<DiscountProductResponse> getBestDiscounts(LocalDate today) {
        List<DiscountEntry> entries = discountEntryRepository.findActiveDiscountsOrderByPercentage(today);

        // Maps each DiscountEntry to a DiscountProductDto
        return entries.stream()
            .map(e -> new DiscountProductResponse(
                e.getProduct().getProductId(),       
                e.getProduct().getProductName(), 
                e.getStore().getName(),             
                e.getPercentageOfDiscount(),       
                e.getProduct().getProductCategory(),  
                e.getProduct().getBrand()           
            ))
            .toList();
    }
}


