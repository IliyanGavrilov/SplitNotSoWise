package bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion;

import bg.sofia.uni.fmi.mjt.splitwise.api.dto.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class ExchangeRates extends BaseResponse {
    private static final String STATUS = "success";
    @SerializedName("conversion_rates")
    private final Map<String, BigDecimal> rates;
    @SerializedName("time_next_update_utc")
    private final LocalDateTime nextUpdateTime;
    private static ExchangeRates instance;

    private ExchangeRates(Map<String, BigDecimal> rates, LocalDateTime nextUpdateTime) {
        super(STATUS);
        this.rates = rates;
        this.nextUpdateTime = nextUpdateTime;
    }

    private ExchangeRates(ExchangeRates other) {
        super(STATUS);
        this.rates = other.rates;
        this.nextUpdateTime = other.nextUpdateTime;
    }

    public static ExchangeRates getInstance(ExchangeRates exchangeRates) {
        if (instance == null) {
            instance = new ExchangeRates(exchangeRates);
        }

        return instance;
    }

    public static ExchangeRates getInstance() {
        return instance;
    }

    public BigDecimal convert(BigDecimal amount, String currency) {
        return rates.get(currency).multiply(amount);
    }

    public boolean hasCurrency(String currency) {
        return rates.containsKey(currency);
    }

    public LocalDateTime getNextUpdateTime() {
        return nextUpdateTime;
    }

    public Set<String> getAvailableCurrencies() {
        return rates.keySet();
    }
}