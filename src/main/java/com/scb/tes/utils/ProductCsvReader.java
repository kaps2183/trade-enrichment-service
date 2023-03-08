package com.scb.tes.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.scb.tes.api.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;

@Component
public class CsvReader implements FeedReader<Product> {

    private final CsvMapper mapper;

    @Autowired
    public CsvReader(CsvMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MappingIterator<Product> readRecordsFrom(Reader reader, Class<Product> pojoType) throws IOException {
        CsvSchema schema = mapper.schemaFor(pojoType).withHeader();
        ObjectReader objectReader = mapper.readerFor(pojoType).with(schema);
        return objectReader.readValues(reader);
    }
}
