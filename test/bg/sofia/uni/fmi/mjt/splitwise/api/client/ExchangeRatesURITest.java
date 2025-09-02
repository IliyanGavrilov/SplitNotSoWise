package bg.sofia.uni.fmi.mjt.splitwise.api.client;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeRatesURITest {
    @Test
    void testBuildUri() throws URISyntaxException {
        ExchangeRatesURI uri = new ExchangeRatesURI.ExchangeRatesURIBuilder("test-api-key").build();
        URI result = uri.buildURI();

        String expected =
            "https://v6.exchangerate-api.com/v6/test-api-key/latest/" + ExchangeRatesURI.DEFAULT_CURRENCY_CODE + '?';
        assertEquals(expected, result.toString(), "URI should match expected mandatory parameter format");
    }
}
