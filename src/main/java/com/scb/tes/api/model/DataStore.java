package com.scb.tes.api.model;

import java.util.List;
import java.util.Map;

public interface DataStore {
    Map<String, Product> getProductStore();
    void addProducts(List<Product> products);
}
