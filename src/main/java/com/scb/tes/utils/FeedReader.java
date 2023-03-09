package com.scb.tes.utils;

import com.fasterxml.jackson.databind.MappingIterator;

import java.io.IOException;
import java.io.Reader;

public interface FeedReader<T> {
    MappingIterator<T> readRecordsFrom(final Reader reader, final Class<T> pojoType) throws IOException;
}
