package bg.sofia.uni.fmi.mjt.splitwise.api.client;

import bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion.ExchangeRates;
import bg.sofia.uni.fmi.mjt.splitwise.api.env.EnvironmentVariableLoader;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.APIKeyInvalidException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.handler.HttpStatusCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExchangeRatesClientTest {
    private static HttpClient mockHttpClient;
    private static HttpResponse<String> mockResponse;
    private static ExchangeRatesClient exchangeRatesClient;

    @BeforeAll
    static void setUp() {
        Reader reader = new StringReader("EXCHANGE_API_KEY=valid-api-key-for-api");
        EnvironmentVariableLoader.loadEnvironmentVariables(reader);
        mockHttpClient = mock();
        mockResponse = mock();

        exchangeRatesClient = ExchangeRatesClient.getInstance(mockHttpClient);
    }

    @Test
    void testGetExchangeRatesSuccess() throws Exception {
        String jsonResponse =
            "{ \"result\": \"success\", \"conversion_rates\": {\"BGN\": 1}, \"time_next_update_utc\": \"Sat, 28 Mar 2020 00:00:00 +0000\" }";
        when(mockResponse.statusCode()).thenReturn(HttpStatusCode.OK.code());
        when(mockResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(
            mockResponse);

        ExchangeRates result = ExchangeRatesClient.getInstance().getExchangeRates();

        assertEquals(BigDecimal.TWO, result.convertFromBaseCurrency(BigDecimal.valueOf(2), "BGN"),
            "Expected conversion rate to be 1!");

        // Test if the status works fine
        assertTrue(result.isSuccess(), "Expected non-error result to return ok status");
    }

    @Test
    void testGetExchangeRatesErrorResponse() throws Exception {
        String errorResponse =
            "{ \"result\": \"error\", \"error-type\": \"invalid-key\" }";
        when(mockResponse.statusCode()).thenReturn(HttpStatusCode.UNAUTHORIZED.code());
        when(mockResponse.body()).thenReturn(errorResponse);
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(
            mockResponse);

        ExchangeAPIException thrown =
            assertThrows(ExchangeAPIException.class, () -> exchangeRatesClient.getExchangeRates(),
                "Expected error response!");
        assertInstanceOf(APIKeyInvalidException.class, thrown.getCause(),
            "Expected exception of type APIKeyInvalidException.");
    }

    @Test
    void testGetExchangeRatesNetworkError() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
            .thenThrow(new RuntimeException("Network error"));

        assertThrows(ExchangeAPIException.class, () -> exchangeRatesClient.getExchangeRates(),
            "Expected ExchangeRatesAPIException to be thrown.");
    }

    @Test
    void testGetExchangeRatesWithCustomParameters() throws Exception {
        String jsonResponse =
            "{ \"result\": \"success\", \"conversion_rates\": {\"BGN\": 1, \"USD\": 2}, \"time_next_update_utc\": \"Sat, 28 Mar 2020 00:00:00 +0000\" }";
        when(mockResponse.statusCode()).thenReturn(HttpStatusCode.OK.code());
        when(mockResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(
            mockResponse);

        ExchangeRates result = exchangeRatesClient.getExchangeRates();

        assertEquals(BigDecimal.TWO, result.convertFromBaseCurrency(BigDecimal.valueOf(2), "BGN"),
            "Expected conversion rate to be 1!");
        assertEquals(BigDecimal.TWO, result.convertFromBaseCurrency(BigDecimal.valueOf(1), "USD"),
            "Expected conversion rate to be 2!");
    }
}
