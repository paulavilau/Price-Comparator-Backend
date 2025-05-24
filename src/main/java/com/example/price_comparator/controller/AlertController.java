package com.example.price_comparator.controller;
import com.example.price_comparator.dto.alert.*;
import com.example.price_comparator.model.PriceAlert;
import com.example.price_comparator.service.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    // Creates a new price alert for a  user email and product id
    @PostMapping("/create-alert")
    public ResponseEntity<String> createAlert(@RequestBody PriceAlertRequest request) {
        Optional<PriceAlert> created = alertService.createAlert(request);
        return created.isPresent()
            ? ResponseEntity.ok("Alertă creată cu succes!")
            : ResponseEntity.status(HttpStatus.CONFLICT).body("⚠️ Alertă deja existentă sau produs inexistent.");
    }

    // Returns active alerts for a specific date
    @GetMapping("/triggered-alert")
    public List<PriceAlertResponse> getTriggeredAlerts(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return alertService.checkAlerts(date);
    }
}
