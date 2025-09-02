package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api;

public class MalformedRequestException extends APIErrorException {
    public MalformedRequestException(String message) {
        super(message);
    }

    public MalformedRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
