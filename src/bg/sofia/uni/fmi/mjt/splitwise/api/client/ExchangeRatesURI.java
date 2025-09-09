package bg.sofia.uni.fmi.mjt.splitwise.api.client;

import java.net.URI;
import java.net.URISyntaxException;

public final class ExchangeRatesURI {
    public static final String DEFAULT_CURRENCY_CODE = "BGN";
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "v6.exchangerate-api.com";
    private static final String API_ENDPOINT_PATH = "/v6/%s/latest/" + DEFAULT_CURRENCY_CODE;

    // Required parameters
    private final String apiKey;

    private ExchangeRatesURI(ExchangeRatesURIBuilder builder) {
        this.apiKey = builder.apiKey;
    }

    public URI buildURI() throws URISyntaxException {
        return new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, String.format(API_ENDPOINT_PATH, apiKey), "", null);
    }

    public static final class ExchangeRatesURIBuilder {
        // Required parameters
        private final String apiKey;

        public ExchangeRatesURIBuilder(String apiKey) {
            this.apiKey = apiKey;
        }

        public ExchangeRatesURI build() {
            return new ExchangeRatesURI(this);
        }
    }
}
