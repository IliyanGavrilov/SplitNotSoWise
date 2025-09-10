package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesURI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    void testConstructorWithDefaultCurrency() {
        User u = new User("alice", "pass", "Ali");
        assertEquals(ExchangeRatesURI.DEFAULT_CURRENCY_CODE, u.getCurrency(),
            "Default currency should be the value of ExchangeRatesURI.DEFAULT_CURRENCY_CODE");
    }
}