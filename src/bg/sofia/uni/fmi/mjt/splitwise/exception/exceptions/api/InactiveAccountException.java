package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api;

public class InactiveAccountException extends APIErrorException {
    public InactiveAccountException(String message) {
        super(message);
    }

    public InactiveAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
