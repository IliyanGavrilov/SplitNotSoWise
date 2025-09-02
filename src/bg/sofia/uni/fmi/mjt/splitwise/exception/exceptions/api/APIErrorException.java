package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;

public class APIErrorException extends ExchangeAPIException {
    public APIErrorException(String message) {
        super(message);
    }

    public APIErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
