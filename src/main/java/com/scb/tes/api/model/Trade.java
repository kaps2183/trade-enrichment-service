package com.scb.tes.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Objects;

@JsonPropertyOrder({"date", "product_id", "currency", "price"})
public class Trade {
    @JsonProperty("date")
    @JsonFormat(pattern = "yyyyMMdd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDate date;
    @JsonProperty("product_id")
    private final String productId;
    @JsonProperty("currency")
    private final String currency;
    @JsonProperty("price")
    private final String price;

    private Trade(LocalDate date,
                  String productId,
                  String currency,
                  String price) {
        this.date = date;
        this.productId = productId;
        this.currency = currency;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getProductId() {
        return productId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPrice() {
        return price;
    }

    @JsonCreator
    public static Trade of(@JsonProperty("date") LocalDate date,
                           @JsonProperty("product_id") String productId,
                           @JsonProperty("currency") String currency,
                           @JsonProperty("price") String price) {
        return new Trade(date, productId, currency, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(date, trade.date) && Objects.equals(productId, trade.productId) && Objects.equals(currency, trade.currency) && Objects.equals(price, trade.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, productId, currency, price);
    }
}