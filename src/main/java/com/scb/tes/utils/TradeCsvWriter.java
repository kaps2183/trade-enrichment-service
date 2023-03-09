package com.scb.tes.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.scb.tes.api.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradeCsvWriter implements FeedWriter<Trade>{

    public static final Logger logger = LoggerFactory.getLogger(TradeCsvWriter.class);
    private final CsvMapper mapper;

    @Autowired
    public TradeCsvWriter(CsvMapper mapper) {
        this.mapper = mapper;
    }

    public  String writeToCsv(List<Trade> trades){
        try {
            return toCsvString(trades);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String toCsvString(List<Trade> trades) throws JsonProcessingException {
        final CsvSchema schema = mapper.schemaFor(Trade.class);
        ObjectWriter writer = mapper.writerFor(List.class).with(schema.withHeader());
        String tradeCsv = writer.writeValueAsString(trades);
        return tradeCsv;
    }
}
