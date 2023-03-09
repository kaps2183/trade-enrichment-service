package com.scb.tes.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.scb.tes.api.model.DataStore;
import com.scb.tes.api.model.Product;
import com.scb.tes.api.model.Trade;
import com.scb.tes.utils.FeedReader;
import com.scb.tes.utils.FeedWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeEnrichmentServiceTest {

    public static final String VALID_TRADE_CSV = "date,product_id,currency,price\n" +
            "20160101,1,EUR,10.0";

    public static final String INVALID_DATE_TRADE_CSV = "date,product_id,currency,price\n" +
            "200101,1,EUR,10.0";
    public static final String ENRICHED_TRADE_CSV = "date,product_id,currency,price\n" +
            "20160101,\"Treasury Bills Domestic\",EUR,10.0";
    public static final String ENRICHED_WITH_STD_MSG = "\"date,product_id,currency,price\\n\" +\n" +
            "            \"20160101,\\\"Missing ProductName\\\",EUR,10.0\"";
    TradeEnrichmentService enrichmentService;
    @Mock
    private FeedWriter feedWriter;
    @Mock
    private FeedReader feedReader;
    @Mock
    private DataStore dataStore;

    @BeforeEach
    void setUp() {
        enrichmentService = new TradeEnrichmentService(feedReader, feedWriter, dataStore);
    }

    @Test
    void shouldEnrichTradesWithProductName_OnValidInput() throws IOException {

        // Given
        Map<String, Product> mockProductStore = mockProductStore();
        mockProductLookup(mockProductStore, "1", "Treasury Bills Domestic");
        MappingIterator<Trade> mappingIterator = mockMappingIterator();
        when(mappingIterator.hasNext()).thenReturn(true).thenReturn(false);
        when(feedWriter.writeToCsv(anyList())).thenReturn(ENRICHED_TRADE_CSV);
        when(mappingIterator.next()).thenReturn(Trade.of(LocalDate.of(2016, 01, 01),
                "1", "EUR", "10.0"));

        //when
        Optional<String> optional = enrichmentService.enrich(VALID_TRADE_CSV);

        //then
        assertThat(optional.isPresent()).isTrue();
        optional.ifPresent(trades ->
                assertThat(trades).isEqualTo(ENRICHED_TRADE_CSV));
        verify(mappingIterator, times(2)).hasNext();
        verify(mappingIterator, times(1)).next();
        verify(feedReader).readRecordsFrom(any(BufferedReader.class), any(Trade.class.getClass()));

    }

    @Test
    void shouldEnrichTradesWith_StdMsg_whenMissingProductMapping() throws IOException {

        //Given
        Map<String, Product> productStore = mockProductStore();
        MappingIterator<Trade> mappingIterator = mockMappingIterator();
        when(mappingIterator.hasNext()).thenReturn(true).thenReturn(false);
        when(mappingIterator.next()).thenReturn(Trade.of(LocalDate.of(2016, 01, 01),
                "1", "EUR", "10.0"));
        when(feedWriter.writeToCsv(anyList())).thenReturn(ENRICHED_WITH_STD_MSG);
        //when
        Optional<String> optional = enrichmentService.enrich(VALID_TRADE_CSV);
        //then
        assertThat(optional.isPresent()).isTrue();
        optional.ifPresent(trades -> {
                    assertThat(trades).isEqualTo(ENRICHED_WITH_STD_MSG);
                    trades.contains("Missing Product Name");
                }
        );
        verify(productStore).get("1");
        verify(mappingIterator, times(2)).hasNext();
        verify(mappingIterator, times(1)).next();
        verify(feedReader).readRecordsFrom(any(BufferedReader.class), any(Trade.class.getClass()));
    }

    @Test
    void shouldSkipRow_OnInvalidDateFormat() throws IOException {

        //Given
        MappingIterator<Trade> mappingIterator = mockMappingIterator();
        when(mappingIterator.hasNext()).thenReturn(false);

        //when
        Optional<String> optional = enrichmentService.enrich(INVALID_DATE_TRADE_CSV);
        assertThat(optional.isPresent()).isFalse();

        //then
        verify(mappingIterator, times(1)).hasNext();
        verify(mappingIterator, times(0)).next();
        verify(feedReader).readRecordsFrom(any(BufferedReader.class), any(Trade.class.getClass()));
    }

    private MappingIterator<Trade> mockMappingIterator() throws IOException {
        MappingIterator<Trade> mockIterator = mock(MappingIterator.class);
        when(feedReader.readRecordsFrom(
                any(BufferedReader.class), any(Trade.class.getClass()))).thenReturn(mockIterator);

        return mockIterator;
    }

    private Map<String, Product> mockProductStore() {
        Map<String, Product> mockProductStore = mock(Map.class);
        when(dataStore.getProductStore()).thenReturn(mockProductStore);
        return mockProductStore;
    }

    private OngoingStubbing<Product> mockProductLookup(Map<String, Product> mockProductStore, String productId, String productName) {
        return when(mockProductStore.get(productId)).thenReturn(Product.of(productId, productName));
    }
}
