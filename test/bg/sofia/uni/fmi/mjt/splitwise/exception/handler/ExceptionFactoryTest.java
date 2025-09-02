package bg.sofia.uni.fmi.mjt.splitwise.exception.handler;

import bg.sofia.uni.fmi.mjt.splitwise.api.dto.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.HttpException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror.BadRequestException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.clienterror.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.servererror.InternalServerErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ExceptionFactoryTest {
    @Test
    void testCreateHttpExceptionWithStatusCodeWithUnexpectedHttpErrorCode() {
        ErrorResponse errorResponse = new ErrorResponse("inactive-account", "API key is invalid");

        assertInstanceOf(HttpException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.MULTI_STATUS, errorResponse),
            "Expected HttpException for unexpected http code!"
        );
    }

    @Test
    void testCreateHttpExceptionBadRequest() {
        ErrorResponse errorResponse = new ErrorResponse("inactive-account", "API key is invalid");

        assertInstanceOf(BadRequestException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.BAD_REQUEST.code(), errorResponse),
            "Expected BadRequestException for HTTP 400 with apiKeyInvalid"
        );
    }

    @Test
    void testCreateHttpExceptionUnauthorized() {
        ErrorResponse errorResponse = new ErrorResponse("quota-reached", "API key is missing");

        assertInstanceOf(UnauthorizedException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.UNAUTHORIZED.code(), errorResponse),
            "Expected UnauthorizedException for HTTP 401 with apiKeyMissing"
        );
    }

    @Test
    void testCreateHttpExceptionTooManyRequests() {
        ErrorResponse errorResponse = new ErrorResponse("rateLimited", "Rate limit exceeded");

        assertInstanceOf(TooManyRequestsException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.TOO_MANY_REQUESTS, errorResponse),
            "Expected TooManyRequestsException for HTTP 429 with rateLimited"
        );

        // Test if the status works fine
        assertEquals(new ErrorResponse("", "").isSuccess(), errorResponse.isSuccess(),
            "Expected error result to return error status");
    }

    @Test
    void testCreateHttpExceptionInternalServerError() {
        ErrorResponse errorResponse = new ErrorResponse("unexpectedError", "Unexpected error occurred");

        assertInstanceOf(InternalServerErrorException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.INTERNAL_SERVER_ERROR.code(), errorResponse),
            "Expected ServerErrorException for HTTP 500 with unexpectedError"
        );
    }

    @Test
    void testCreateHttpExceptionUnexpectedError() {
        ErrorResponse errorResponse = new ErrorResponse("unknownCode", "Unknown API error");

        assertInstanceOf(HttpException.class,
            ExceptionFactory.createHttpException(1234, errorResponse),
            "Expected HttpException for unexpected HTTP status code 1234"
        );
    }

    @Test
    void testCreateHttpExceptionUnauthorizedWithDisabledKey() {
        ErrorResponse errorResponse = new ErrorResponse("apiKeyDisabled", "Unknown API error");

        assertInstanceOf(HttpException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.UNAUTHORIZED.code(), errorResponse),
            "Expected HttpException for unexpected HTTP status code 401"
        );
    }

    @Test
    void testCreateHttpExceptionUnauthorizedWithExhaustedApiKey() {
        ErrorResponse errorResponse = new ErrorResponse("unsupported-code", "Unknown API error");

        assertInstanceOf(HttpException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.UNAUTHORIZED.code(), errorResponse),
            "Expected HttpException for unexpected HTTP status code 401"
        );
    }

    @Test
    void testCreateHttpExceptionBadRequestWithInvalidParameter() {
        ErrorResponse errorResponse = new ErrorResponse("malformed-request", "API key is invalid");

        assertInstanceOf(BadRequestException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.BAD_REQUEST.code(), errorResponse),
            "Expected BadRequestException for HTTP 400 with apiKeyInvalid"
        );
    }

    @Test
    void testCreateHttpExceptionBadRequestWithMissingParameters() {
        ErrorResponse errorResponse = new ErrorResponse("invalid-key", "API key is invalid");

        assertInstanceOf(BadRequestException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.BAD_REQUEST.code(), errorResponse),
            "Expected BadRequestException for HTTP 400 with apiKeyInvalid"
        );
    }

    @Test
    void testCreateHttpExceptionBadRequestWithInvalidCode() {
        ErrorResponse errorResponse = new ErrorResponse("1234", "API key is invalid");

        assertInstanceOf(BadRequestException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.BAD_REQUEST.code(), errorResponse),
            "Expected BadRequestException for HTTP 400 with apiKeyInvalid"
        );
    }

    @Test
    void testCreateHttpExceptionBadRequestWithNullCode() {
        ErrorResponse errorResponse = new ErrorResponse(null, "API key is invalid");

        assertInstanceOf(BadRequestException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.BAD_REQUEST.code(), errorResponse),
            "Expected BadRequestException for HTTP 400 with apiKeyInvalid"
        );
    }

    @Test
    void testCreateHttpExceptionBadRequestWithNullMessage() {
        ErrorResponse errorResponse = new ErrorResponse("1", null);

        assertInstanceOf(BadRequestException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.BAD_REQUEST.code(), errorResponse),
            "Expected BadRequestException for HTTP 400 with apiKeyInvalid"
        );
    }

    @Test
    void testCreateHttpExceptionBadRequestWithBlankCode() {
        ErrorResponse errorResponse = new ErrorResponse("", "API key is invalid");

        assertInstanceOf(BadRequestException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.BAD_REQUEST.code(), errorResponse),
            "Expected BadRequestException for HTTP 400 with apiKeyInvalid"
        );
    }

    @Test
    void testCreateHttpExceptionBadRequestWithBlankMessage() {
        ErrorResponse errorResponse = new ErrorResponse("1", "");

        assertInstanceOf(BadRequestException.class,
            ExceptionFactory.createHttpException(HttpStatusCode.BAD_REQUEST.code(), errorResponse),
            "Expected BadRequestException for HTTP 400 with apiKeyInvalid"
        );
    }
}

