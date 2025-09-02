package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.HttpException;

public class UnauthorizedException extends HttpException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
