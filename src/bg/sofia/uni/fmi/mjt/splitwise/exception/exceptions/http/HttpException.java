package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;

public class HttpException extends ExchangeAPIException {
    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
