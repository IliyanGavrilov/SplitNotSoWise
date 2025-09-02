package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.HttpException;

public class TooManyRequestsException extends HttpException {
    public TooManyRequestsException(String message) {
        super(message);
    }

    public TooManyRequestsException(String message, Throwable cause) {
        super(message, cause);
    }
}
