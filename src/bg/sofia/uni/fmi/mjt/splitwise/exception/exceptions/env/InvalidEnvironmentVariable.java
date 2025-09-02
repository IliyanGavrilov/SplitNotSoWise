package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.env;

public class InvalidEnvironmentVariable extends RuntimeException {
    public InvalidEnvironmentVariable(String message) {
        super(message);
    }

    public InvalidEnvironmentVariable(String message, Throwable cause) {
        super(message, cause);
    }
}
