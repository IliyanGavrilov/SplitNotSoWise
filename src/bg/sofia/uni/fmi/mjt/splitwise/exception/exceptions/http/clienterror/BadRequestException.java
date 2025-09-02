package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.HttpException;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
