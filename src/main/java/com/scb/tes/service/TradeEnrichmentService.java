package com.scb.tes.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.scb.tes.api.model.DataStore;
import com.scb.tes.api.model.Product;
import com.scb.tes.api.model.Trade;
import com.scb.tes.utils.FeedReader;
import com.scb.tes.utils.FeedWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TradeEnrichmentService {

    public static final Logger logger = LoggerFactory.getLogger(TradeEnrichmentService.class);
    public static final String MISSING_PRODUCT_NAME = "Missing Product Name";
    private final FeedReader<Trade> tradeFeedReader;
    private final FeedWriter<Trade> tradeFeedWriter;
    private final DataStore dataStore;

    @Autowired
    public TradeEnrichmentService(FeedReader<Trade> tradeFeedReader, FeedWriter<Trade> tradeFeedWriter, DataStore dataStore) {
        this.tradeFeedReader = tradeFeedReader;
        this.tradeFeedWriter = tradeFeedWriter;
        this.dataStore = dataStore;
    }

    public Optional<String> enrich(String tradeCsv) throws IOException {
        List<Trade> enrichedTrades = enrichTrades(tradeCsv);
        logger.debug("enriched trades -> {}", enrichedTrades);
        String csvTrades = tradeFeedWriter.writeToCsv(enrichedTrades);
        if (StringUtils.hasText(csvTrades)) {
            return Optional.of(csvTrades);
        } else {
            return Optional.empty();
        }
    }

    private Trade enrichTradeWithProductName(Trade trade) {
        Product product = dataStore.getProductStore().get(trade.getProductId());
        String productName;
        if (product != null) {
            productName = product.getProductName();
            logger.info("found mapping product name {} for product id {}", productName, trade.getProductId());
        } else {
            logger.error("Mapping not found for product id {} ", trade.getProductId());
            productName = MISSING_PRODUCT_NAME;
        }
        return Trade.of(
                trade.getDate(),
                productName,
                trade.getCurrency(),
                trade.getPrice());
    }

    private List<Trade> enrichTrades(String tradeCsv) throws IOException {

        MappingIterator<Trade> tradeMappingIterator = readFromCsv(tradeCsv);

        List<Trade> trades = new ArrayList<>();
        while (tradeMappingIterator.hasNext()) {
            try {
                Trade trade = tradeMappingIterator.next();
                trades.add(enrichTradeWithProductName(trade));
            } catch (RuntimeJsonMappingException e) {
                logger.error("Invalid Date format. Skipping this record.");
            }
        }
        return trades;
    }

    private MappingIterator<Trade> readFromCsv(String tradeCsv) throws IOException {
        MappingIterator<Trade> tradeMappingIterator = tradeFeedReader.readRecordsFrom(new BufferedReader(new StringReader(tradeCsv)), Trade.class);
        return tradeMappingIterator;
    }
}

