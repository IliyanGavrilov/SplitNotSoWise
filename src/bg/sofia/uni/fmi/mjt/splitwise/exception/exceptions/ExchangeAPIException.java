package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions;

public class ExchangeAPIException extends ExpenseSplitException {
    public ExchangeAPIException(String message) {
        super(message);
    }

    public ExchangeAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
