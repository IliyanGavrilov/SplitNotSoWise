package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api;

public class APIKeyExhaustedException extends APIErrorException {
    public APIKeyExhaustedException(String message) {
        super(message);
    }

    public APIKeyExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }
}
