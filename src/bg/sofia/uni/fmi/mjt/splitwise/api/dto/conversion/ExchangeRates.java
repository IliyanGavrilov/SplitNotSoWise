package bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion;

import bg.sofia.uni.fmi.mjt.splitwise.api.dto.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.Expense.BIG_DECIMAL_SCALE;

public final class ExchangeRates extends BaseResponse {
    private static final String STATUS = "success";
    @SerializedName("conversion_rates")
    private final Map<String, BigDecimal> rates;
    @SerializedName("time_next_update_utc")
    private final LocalDateTime nextUpdateTime;
    private static ExchangeRates instance;

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

    // Input
    public BigDecimal convertToBaseCurrency(BigDecimal amount, String currency) {
        return amount.divide(rates.get(currency), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
    }

    // Output
    public BigDecimal convertFromBaseCurrency(BigDecimal amount, String currency) {
        return amount.multiply(rates.get(currency));
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