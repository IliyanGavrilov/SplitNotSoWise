//package bg.sofia.uni.fmi.mjt.splitwise.exception.handler;
//
//import bg.sofia.uni.fmi.mjt.splitwise.dto.error.ErrorResponse;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.APIEndpointNotFoundException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.APIErrorException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.APIKeyExhaustedException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.APIKeyInvalidOrMissingException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.AmountMissingOrInvalidException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.EndDateMissingOrInvalidException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.InactiveAccountException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.InsufficientSubscriptionException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.InvalidBaseCurrencyException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.InvalidDateException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.InvalidSymbolsException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.NoDateSpecifiedException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.NoResultsForRequestException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.ResourceNotFoundException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.StartDateMissingOrInvalidException;
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.api.TimeframeMissingOrInvalidException;
//
//public enum ApiError {
//    // Pretty sure these codes have almost nothing to do with HTTP status codes, but I'm still using them
//    API_KEY_INVALID_OR_MISSING(HttpStatusCode.SWITCHING_PROTOCOLS.code()),
//    INACTIVE_ACCOUNT(HttpStatusCode.PROCESSING.code()),
//    API_ENDPOINT_NOT_FOUND(HttpStatusCode.EARLY_HINTS.code()),
//    API_KEY_EXHAUSTED(104),
//    INSUFFICIENT_SUBSCRIPTION(105),
//    NO_RESULTS_FOR_REQUEST(106),
//    INVALID_BASE_CURRENCY(HttpStatusCode.CREATED.code()),
//    INVALID_SYMBOLS(HttpStatusCode.ACCEPTED.code()),
//    NO_DATE_SPECIFIED(HttpStatusCode.MOVED_PERMANENTLY.code()),
//    INVALID_DATE(HttpStatusCode.FOUND.code()),
//    AMOUNT_MISSING_OR_INVALID(HttpStatusCode.FORBIDDEN.code()),
//    RESOURCE_NOT_FOUND(HttpStatusCode.NOT_FOUND.code()),
//    TIMEFRAME_MISSING_OR_INVALID(HttpStatusCode.NOT_IMPLEMENTED.code()),
//    START_DATE_MISSING_OR_INVALID(HttpStatusCode.BAD_GATEWAY.code()),
//    END_DATE_MISSING_OR_INVALID(HttpStatusCode.SERVICE_UNAVAILABLE.code()),
//    TIMEFRAME_TOO_LONG(HttpStatusCode.VERSION_NOT_SUPPORTED.code());
//
//    private final int code;
//
//    private static APIErrorException createException(int code, String message) {
//        ApiError error = getFromCode(code);
//
//        return switch (error) {
//            case API_KEY_INVALID_OR_MISSING -> new APIKeyInvalidOrMissingException(message);
//            case INACTIVE_ACCOUNT -> new InactiveAccountException(message);
//            case API_ENDPOINT_NOT_FOUND -> new APIEndpointNotFoundException(message);
//            case API_KEY_EXHAUSTED -> new APIKeyExhaustedException(message);
//            case INSUFFICIENT_SUBSCRIPTION -> new InsufficientSubscriptionException(message);
//            case NO_RESULTS_FOR_REQUEST -> new NoResultsForRequestException(message);
//            case INVALID_BASE_CURRENCY -> new InvalidBaseCurrencyException(message);
//            case INVALID_SYMBOLS -> new InvalidSymbolsException(message);
//            case NO_DATE_SPECIFIED -> new NoDateSpecifiedException(message);
//            case INVALID_DATE -> new InvalidDateException(message);
//            case AMOUNT_MISSING_OR_INVALID -> new AmountMissingOrInvalidException(message);
//            case RESOURCE_NOT_FOUND -> new ResourceNotFoundException(message);
//            case TIMEFRAME_MISSING_OR_INVALID -> new TimeframeMissingOrInvalidException(message);
//            case START_DATE_MISSING_OR_INVALID -> new StartDateMissingOrInvalidException(message);
//            case END_DATE_MISSING_OR_INVALID -> new EndDateMissingOrInvalidException(message);
//            case TIMEFRAME_TOO_LONG -> new TimeFrame(message);
//        }
//    }
//
//    ApiError(int code) {
//        this.code = code;
//    }
//
//    public static APIErrorException createException(ErrorResponse errorResponse) {
//        return createException(errorResponse.getCode(), errorResponse.getMessage());
//    }
//
//    public static ApiError getFromCode(int code) {
//        for (ApiError error : values()) {
//            if (error.code == code) {
//                return error;
//            }
//        }
//
//        return null;
//        // throw new APIErrorException("Unknown API error code: " + code); TODO
//    }
//}
