package com.example.price_comparator.service;
import com.example.price_comparator.dto.alert.PriceAlertRequest;
import com.example.price_comparator.dto.alert.PriceAlertResponse;
import com.example.price_comparator.model.*;
import com.example.price_comparator.repository.*;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AlertService {

    private final PriceEntryRepository priceEntryRepository;
    private final ProductRepository productRepository;
    private final PriceAlertRepository alertRepository;
    private final JavaMailSender mailSender;

    public AlertService(PriceEntryRepository priceEntryRepository, ProductRepository productRepository, PriceAlertRepository alertRepository, JavaMailSender mailSender) {
        this.priceEntryRepository = priceEntryRepository;
        this.productRepository = productRepository;
        this.alertRepository = alertRepository;
        this.mailSender = mailSender;
    }

public Optional<PriceAlert> createAlert(PriceAlertRequest request) {
    Optional<Product> optionalProduct = productRepository.findById(request.productId());

    if (optionalProduct.isEmpty()) {
        System.out.println("Niciun produs gasit pentru ID-ul: " + request.productId());
        return Optional.empty();
    }

    Product product = optionalProduct.get();

    // We veriify if an alert already exists
    boolean alreadyExists = alertRepository.existsByUserEmailAndProductAndTargetPriceAndTriggeredFalse(
        request.userEmail(), product, request.targetPrice()
    );

    if (alreadyExists) {
        System.out.println("Alerta deja existenta pentru utilizator: " + request.userEmail());
        return Optional.empty();
    }

    PriceAlert newAlert = new PriceAlert(request.userEmail(), product, request.targetPrice());
    alertRepository.save(newAlert);
    System.out.println("Alerta salvata pentru: " + product.getProductName());

    return Optional.of(newAlert);
}


    // Checks all untriggered alerts and if the price is belowe the target price, it triggers the alert
    public List<PriceAlertResponse> checkAlerts(LocalDate date) {
        List<PriceAlertResponse> responses = new ArrayList<>();

        for (PriceAlert alert : alertRepository.findByTriggeredFalse()) {
            List<PriceEntry> entries = priceEntryRepository.findByProductAndDate(alert.getProduct(), date);
            entries.stream()
                .min(Comparator.comparingDouble(PriceEntry::getPrice))
                .ifPresent(minEntry -> {
                    if (minEntry.getPrice() <= alert.getTargetPrice()) {
                        // Alert is marked as triggered so that is isnt triggered multiple times
                        alert.setTriggered(true);
                        responses.add(new PriceAlertResponse(
                            alert.getProduct().getProductId(),
                            alert.getProduct().getProductName(),
                            minEntry.getStore().getName(),
                            minEntry.getPrice(),
                            alert.getTargetPrice(),
                            minEntry.getCurrency(),
                            alert.getUserEmail()
                        ));
                        System.out.println("Alert triggered for product: " + alert.getProduct().getProductName() +
                            " at store: " + minEntry.getStore().getName() +
                            " with price: " + minEntry.getPrice());
                    }
                });
        }

        alertRepository.saveAll(alertRepository.findByTriggeredFalse());
        return responses;
    }

    // This method is scheduled to run every minute and checks for alerts
    // For testing purposes, normally the interval would be longer
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void checkAndNotify() {
        LocalDate today = LocalDate.now();
        List<PriceAlertResponse> triggered = checkAlerts(today);

        for (PriceAlertResponse alert : triggered) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(alert.userEmail()); // poți extrage emailul real din alert dacă îl incluzi în DTO
            message.setSubject("Reducere activata: " + alert.productName());
            message.setText("""
                    Salut!
                    
                    Produsul "%s" a scazut sub pretul pretul țintă: %.2f %s.
                    Magazin: %s
                    Preț actual: %.2f %s

                    Spor la cumparaturi!
                    """.formatted(
                    alert.productName(),
                    alert.targetPrice(),
                    alert.currency(),
                    alert.store(),
                    alert.targetPrice(),
                    alert.currency()
            ));

            mailSender.send(message);
            System.out.println("Email sent to: vilaupaula@yahoo.ro"+
                " for product: " + alert.productName() +
                " at store: " + alert.store() +
                " with price: " + alert.targetPrice());
        }
    }

}
