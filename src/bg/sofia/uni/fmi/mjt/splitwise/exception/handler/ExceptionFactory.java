package bg.sofia.uni.fmi.mjt.splitwise.exception.handler;

import bg.sofia.uni.fmi.mjt.splitwise.api.dto.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.APIErrorException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.APIKeyExhaustedException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.APIKeyInvalidException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.InactiveAccountException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.MalformedRequestException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.UnsupportedCurrencyCodeException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.HttpException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror.BadRequestException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.servererror.InternalServerErrorException;

public class ExceptionFactory {
    public static HttpException createHttpException(int statusCode, ErrorResponse errorResponse) {
        try {
            return createHttpException(HttpStatusCode.getFromCode(statusCode), errorResponse);
        } catch (HttpException e) {
            return e;
        }
    }

    public static HttpException createHttpException(HttpStatusCode httpStatusCode, ErrorResponse errorResponse) {
        APIErrorException apiErrorException =
            createApiErrorException(errorResponse.getCode());

        return switch (httpStatusCode) {
            case HttpStatusCode.BAD_REQUEST ->
                new BadRequestException(HttpStatusCode.BAD_REQUEST.message(), apiErrorException);
            case HttpStatusCode.UNAUTHORIZED ->
                new UnauthorizedException(HttpStatusCode.UNAUTHORIZED.message(), apiErrorException);
            case HttpStatusCode.TOO_MANY_REQUESTS ->
                new TooManyRequestsException(HttpStatusCode.TOO_MANY_REQUESTS.message(), apiErrorException);
            case HttpStatusCode.INTERNAL_SERVER_ERROR ->
                new InternalServerErrorException(HttpStatusCode.INTERNAL_SERVER_ERROR.message(), apiErrorException);
            default -> new HttpException(String.format("Unexpected HTTP error: %s", httpStatusCode), apiErrorException);
        };
    }

    private static APIErrorException createApiErrorException(String code) {
        try {
            return switch (code) {
                case "unsupported-code" ->
                    new UnsupportedCurrencyCodeException(code + ": Invalid or unsupported currency code!");
                case "malformed-request" -> new MalformedRequestException(code + ": Malformed URI request!");
                case "invalid-key" -> new APIKeyInvalidException(code + ": Invalid API key!");
                case "inactive-account" ->
                    new InactiveAccountException(code + ": Inactive account! Your email address wasn't confirmed!");
                case "quota-reached" -> new APIKeyExhaustedException(code + ": API key requests exhausted!");
                default -> new APIErrorException(String.format("Unknown code: %s", code));
            };
        } catch (NullPointerException e) {
            return new APIErrorException("API error code is null!", e);
        }
    }
}