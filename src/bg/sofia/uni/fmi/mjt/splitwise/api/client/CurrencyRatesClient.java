package bg.sofia.uni.fmi.mjt.splitwise.api.client;

import bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion.ExchangeRates;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;

public interface CurrencyRatesClient {
    ExchangeRates getExchangeRates() throws ExchangeAPIException;
}
 