package com.scb.tes.api.controller;

import com.scb.tes.service.TradeEnrichmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TradeEnrichmentController {

    public static final Logger logger = LoggerFactory.getLogger(TradeEnrichmentController.class);
    private TradeEnrichmentService tradeEnrichmentService;

    @Autowired
    public TradeEnrichmentController(TradeEnrichmentService tradeEnrichmentService) {
        this.tradeEnrichmentService = tradeEnrichmentService;
    }

    @PostMapping(value = "/v1/enrich", consumes = "text/csv", produces = "text/csv")
    public ResponseEntity<String> enrich(@RequestBody String tradeCsv) {

        if (!StringUtils.hasText(tradeCsv)){
            return new ResponseEntity<>("Empty Data",(HttpStatus.BAD_REQUEST));
        }
        logger.info("Received Trade -> {}", tradeCsv);
        Optional<String> enrichedTrades = null;
        try {
            enrichedTrades = tradeEnrichmentService.enrich(tradeCsv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("enriched trades {}", enrichedTrades.get());
        return new ResponseEntity<>(enrichedTrades.get(),HttpStatus.OK);
    }

}
