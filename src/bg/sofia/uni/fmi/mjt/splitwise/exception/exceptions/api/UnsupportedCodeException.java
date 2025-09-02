package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api;

public class UnsupportedCodeException extends APIErrorException {
    public UnsupportedCodeException(String message) {
        super(message);
    }

    public UnsupportedCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
