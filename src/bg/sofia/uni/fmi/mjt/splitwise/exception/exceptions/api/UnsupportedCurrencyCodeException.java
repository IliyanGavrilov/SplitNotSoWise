package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api;

public class UnsupportedCurrencyCodeException extends APIErrorException {
    public UnsupportedCurrencyCodeException(String message) {
        super(message);
    }

    public UnsupportedCurrencyCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
