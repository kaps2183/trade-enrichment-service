package com.scb.tes.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.scb.tes.api.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;

@Component
public class TradeCsvReader implements FeedReader<Trade> {

    private CsvMapper mapper;

    @Autowired
    public TradeCsvReader(CsvMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MappingIterator<Trade> readRecordsFrom(Reader reader, Class<Trade> pojoType) throws IOException {
        final CsvSchema schema = mapper.schemaFor(pojoType);
        ObjectReader objectReader = mapper.readerFor(pojoType).with(schema.withHeader());
        return  objectReader.readValues(reader) ;
    }
}
