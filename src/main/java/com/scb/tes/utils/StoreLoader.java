package com.scb.tes.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.scb.tes.api.model.DataStore;
import com.scb.tes.api.model.EnrichmentDataStore;
import com.scb.tes.api.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StoreLoader implements CommandLineRunner {

    public static final Logger logger = LoggerFactory.getLogger(StoreLoader.class);
    private final FeedReader<Product> productFeedReader;
    private final Path productFilePath;
    private final DataStore dataStore;


    @Autowired
    public StoreLoader(@Value("classpath:static/product.csv") Path productFilePath,
                       FeedReader<Product> productFeedReader, DataStore dataStore) {
        this.productFilePath = productFilePath;
        this.productFeedReader = productFeedReader;
        this.dataStore = dataStore;
    }

    @Override
    public void run(String... args) throws Exception {
        loadProducts();
    }

    private void loadProducts() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(productFilePath)) {
            MappingIterator<Product> productMappingIterator = productFeedReader.readRecordsFrom(bufferedReader, Product.class);
            dataStore.addProducts(productMappingIterator.readAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
