package com.scb.tes.utils;

import com.fasterxml.jackson.databind.MappingIterator;
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
public class ProductLoader implements CommandLineRunner {

    public static final Logger logger = LoggerFactory.getLogger(ProductLoader.class);
    private final FeedReader<Product> productFeedReader;
    private final Path productFilePath;

    @Autowired
    public ProductLoader(@Value("classpath:static/product.csv") Path productFilePath,
                         FeedReader<Product> productFeedReader) {
        this.productFilePath = productFilePath;
        this.productFeedReader = productFeedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        loadProducts();
    }

    private void loadProducts() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(productFilePath)) {
            MappingIterator<Product> productMappingIterator = productFeedReader.readRecordsFrom(bufferedReader, Product.class);
            logger.info("loaded products {}", productMappingIterator.readAll().size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
