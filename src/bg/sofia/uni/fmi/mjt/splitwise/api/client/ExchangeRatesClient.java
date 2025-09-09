package bg.sofia.uni.fmi.mjt.splitwise.api.client;

import bg.sofia.uni.fmi.mjt.splitwise.api.dto.BaseResponse;
import bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion.ExchangeRates;
import bg.sofia.uni.fmi.mjt.splitwise.api.dto.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.splitwise.api.env.EnvironmentVariableLoader;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.env.InvalidEnvironmentVariable;
import bg.sofia.uni.fmi.mjt.splitwise.exception.handler.ExceptionFactory;
import bg.sofia.uni.fmi.mjt.splitwise.exception.handler.HttpStatusCode;
import bg.sofia.uni.fmi.mjt.splitwise.util.json.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public final class ExchangeRatesClient implements CurrencyRatesClient {
    private static final String API_KEY_NAME = "EXCHANGE_API_KEY";
    private static String apiKey;
    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();
    private final HttpClient exchangeHttpClient;
    private static ExchangeRatesClient instance;

    static {
        try {
            EnvironmentVariableLoader.loadEnvironmentVariables();
            apiKey = EnvironmentVariableLoader.get(API_KEY_NAME);
        } catch (InvalidEnvironmentVariable e) {
            apiKey = "";
        }
    }

    private ExchangeRates updateExchangeRates() throws ExchangeAPIException {
        if (apiKey.isBlank()) {
            throw new ExchangeAPIException("API key missing!");
        }

        HttpResponse<String> response;

        try {
            URI uri = new ExchangeRatesURI.ExchangeRatesURIBuilder(apiKey)
                .build()
                .buildURI();

            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

            response = exchangeHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new ExchangeAPIException("Could not retrieve exchange rates!", e);
        }

        BaseResponse br = GSON.fromJson(response.body(), BaseResponse.class);

        if (response.statusCode() == HttpStatusCode.OK.code() && br.isSuccess()) {
            ExchangeRates.getInstance(GSON.fromJson(response.body(), ExchangeRates.class));

            return ExchangeRates.getInstance();
        }

        throw ExceptionFactory.createHttpException(response.statusCode(),
            GSON.fromJson(response.body(), ErrorResponse.class));
    }

    private ExchangeRatesClient(HttpClient exchangeHttpClient) {
        this.exchangeHttpClient = exchangeHttpClient;
    }

    private ExchangeRatesClient(HttpClient exchangeHttpClient, String apiKey) {
        this(exchangeHttpClient);
        ExchangeRatesClient.apiKey = apiKey;
    }

    public static ExchangeRatesClient getInstance(HttpClient exchangeHttpClient) {
        if (instance == null) {
            instance = new ExchangeRatesClient(exchangeHttpClient);
        }

        return instance;
    }

    public static ExchangeRatesClient getInstance(HttpClient exchangeHttpClient, String apiKey) {
        if (instance == null) {
            instance = new ExchangeRatesClient(exchangeHttpClient, apiKey);
        }

        return instance;
    }

    public static ExchangeRatesClient getInstance() {
        return instance;
    }

    @Override
    public ExchangeRates getExchangeRates() throws ExchangeAPIException {
        ExchangeRates exchangeRates = ExchangeRates.getInstance();

        if (exchangeRates == null || LocalDateTime.now().isAfter(exchangeRates.getNextUpdateTime())) {
            return updateExchangeRates();
        }

        return exchangeRates;
    }
}
