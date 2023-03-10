package com.scb.tes.api.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EnrichmentDataStore implements DataStore{
    public static final Logger logger = LoggerFactory.getLogger(EnrichmentDataStore.class);
    private final ConcurrentHashMap<String, Product> productStore = new ConcurrentHashMap<>();

    public void addProducts(List<Product> products) {
        logger.info("Adding {} Products to datastore ->", products.size());
        products.forEach(product -> productStore.putIfAbsent(product.getProductId(), product));
    }

    public Map<String, Product> getProductStore() {
        return Collections.unmodifiableMap(productStore);
    }

    public void reset() {
        productStore.clear();
    }
}
