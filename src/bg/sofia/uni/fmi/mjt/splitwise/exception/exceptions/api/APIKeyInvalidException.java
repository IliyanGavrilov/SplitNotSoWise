package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api;

public class APIKeyInvalidException extends APIErrorException {
    public APIKeyInvalidException(String message) {
        super(message);
    }

    public APIKeyInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
