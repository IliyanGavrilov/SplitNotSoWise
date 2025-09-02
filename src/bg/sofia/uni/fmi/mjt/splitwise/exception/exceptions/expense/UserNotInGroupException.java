package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.expense;

public class UserNotInGroupException extends RuntimeException {
    public UserNotInGroupException(String message) {
        super(message);
    }

    public UserNotInGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
