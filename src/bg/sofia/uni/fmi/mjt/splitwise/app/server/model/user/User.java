package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesURI;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.UnsupportedCurrencyCodeException;

public class User {
    private final String username;
    private final String password;
    private final String nickname;
    private String currency; // Not a record because of this

    public User(String username, String password, String nickname, String currency) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.currency = currency;
    }

    public User(String username, String password, String nickname) {
        this(username, password, nickname, ExchangeRatesURI.DEFAULT_CURRENCY_CODE);
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) throws ExchangeAPIException {
        if (ExchangeRatesClient.getInstance().getExchangeRates().hasCurrency(currency)) {
            this.currency = currency;
        }

        throw new UnsupportedCurrencyCodeException("Given currency code is not supported!");
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
