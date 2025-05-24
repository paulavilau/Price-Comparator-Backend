package com.example.price_comparator.service;

import com.example.price_comparator.model.*;
import com.example.price_comparator.repository.*;
import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final PriceEntryRepository priceEntryRepository;
    private final DiscountEntryRepository discountEntryRepository;

    private static final String DATA_FOLDER = "data";

     // Runs when the application starts
     // Imports csv files from the data folder
    @PostConstruct
    public void importCsvFiles() throws Exception {
        Path folderPath = Paths.get(new ClassPathResource(DATA_FOLDER).getURI());

        try (DirectoryStream<Path> files = Files.newDirectoryStream(folderPath)) {
            for (Path file : files) {
                String fileName = file.getFileName().toString();

                if (fileName.endsWith(".csv")) {
                    if (fileName.contains("discount")) {
                        importDiscountFile(file.toFile());
                    } else {
                        importPriceFile(file.toFile());
                    }
                }
            }
        }
    }

     // Imports a price file
     // If there is already an entry for the product, store and date, it updates the price
     // Otherwise, it saves a new entry
    private void importPriceFile(File file) throws Exception {
        String fileName = file.getName();
        String[] parts = fileName.replace(".csv", "").split("_");

        String storeName = parts[0].toLowerCase();
        LocalDate date = LocalDate.parse(parts[1]);

        // Creates or retrieves the store
        Store store = storeRepository.findById(storeName).orElseGet(() -> {
            Store s = new Store(storeName);
            return storeRepository.save(s);
        });

        try (CSVReader reader = new CSVReader(new FileReader(file, java.nio.charset.StandardCharsets.UTF_8))) {
            String[] header = reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                // Extracts information from the file
                String productId = line[0];
                String productName = line[1];
                String category = line[2];
                String brand = line[3];
                double quantity = Double.parseDouble(line[4]);
                String unit = line[5];
                double price = Double.parseDouble(line[6]);
                String currency = line[7];

                // Creats or retrieves the product
                Product product = productRepository.findById(productId).orElseGet(() -> {
                    Product p = new Product(productId, productName, category, brand, quantity, unit);
                    return productRepository.save(p);
                });

                // If the product already exists, we update the price
                Optional<PriceEntry> existing = priceEntryRepository
                        .findByProductAndStoreAndDate(product, store, date);

                if (existing.isPresent()) {
                    PriceEntry entry = existing.get();
                    entry.setPrice(price);
                    entry.setCurrency(currency);
                    priceEntryRepository.save(entry);
                } else {
                    PriceEntry entry = new PriceEntry(null, product, store, date, price, currency);
                    priceEntryRepository.save(entry);
                }
            }
        }
    }

     // Imports a discount file
     // Each lines in the file represents a discount for a product in a store during a specific period
    private void importDiscountFile(File file) throws Exception {
        String fileName = file.getName();
        String[] parts = fileName.replace(".csv", "").split("_");

        String storeName = parts[0].toLowerCase();
        LocalDate importDate = LocalDate.parse(parts[2]); // Data extrasa din numele fisierului

        Store store = storeRepository.findById(storeName).orElseGet(() -> {
            Store s = new Store(storeName);
            return storeRepository.save(s);
        });

        try (CSVReader reader = new CSVReader(new FileReader(file, java.nio.charset.StandardCharsets.UTF_8))) {
            String[] header = reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                // Extracts information from the file
                String productId = line[0];
                String productName = line[1];
                String brand = line[2];
                double quantity = Double.parseDouble(line[3]);
                String unit = line[4];
                String category = line[5];
                LocalDate fromDate = LocalDate.parse(line[6]);
                LocalDate toDate = LocalDate.parse(line[7]);
                double percentage = Double.parseDouble(line[8]);

                Product product = productRepository.findById(productId).orElseGet(() -> {
                    Product p = new Product(productId, productName, category, brand, quantity, unit);
                    return productRepository.save(p);
                });

                Optional<DiscountEntry> existing = discountEntryRepository
                        .findByProductAndStoreAndFromDateAndToDate(product, store, fromDate, toDate);

                if (existing.isPresent()) {
                    DiscountEntry discount = existing.get();
                    discount.setPercentageOfDiscount(percentage);
                    discountEntryRepository.save(discount);
                } else {
                    DiscountEntry discount = new DiscountEntry(
                        null, product, store, fromDate, toDate, importDate, percentage);
                    discountEntryRepository.save(discount);
                }
            }
        }
    }
}

