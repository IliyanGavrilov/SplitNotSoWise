package bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.servererror;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.HttpException;

public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
