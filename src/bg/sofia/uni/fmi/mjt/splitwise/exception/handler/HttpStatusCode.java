package bg.sofia.uni.fmi.mjt.splitwise.exception.handler;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.http.HttpException;

import java.net.HttpURLConnection;

public enum HttpStatusCode {
    // 1XX - Informational Response
    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    PROCESSING(102, "Processing"),
    EARLY_HINTS(103, "Early Hints"),

    // 2XX - Success
    OK(HttpURLConnection.HTTP_OK, "OK"),
    CREATED(HttpURLConnection.HTTP_CREATED, "Created"),
    ACCEPTED(HttpURLConnection.HTTP_ACCEPTED, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(HttpURLConnection.HTTP_NOT_AUTHORITATIVE, "Non-Authoritative Information"),
    NO_CONTENT(HttpURLConnection.HTTP_NO_CONTENT, "No Content"),
    RESET_CONTENT(HttpURLConnection.HTTP_RESET, "Reset Content"),
    PARTIAL_CONTENT(HttpURLConnection.HTTP_PARTIAL, "Partial Content"),
    MULTI_STATUS(207, "Multi-Status"),
    ALREADY_REPORTED(208, "Already Reported"),
    IM_USED(226, "IM Used"),

    // 3XX - Redirection
    MULTIPLE_CHOICES(HttpURLConnection.HTTP_MULT_CHOICE, "Multiple Choices"),
    MOVED_PERMANENTLY(HttpURLConnection.HTTP_MOVED_PERM, "Moved Permanently"),
    FOUND(HttpURLConnection.HTTP_MOVED_TEMP, "Found"), // Moved Temporarily
    SEE_OTHER(HttpURLConnection.HTTP_SEE_OTHER, "See Other"),
    NOT_MODIFIED(HttpURLConnection.HTTP_NOT_MODIFIED, "Not Modified"),
    USE_PROXY(HttpURLConnection.HTTP_USE_PROXY, "Use Proxy"),
    SWITCH_PROXY(306, "Switch Proxy"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),

    // 4XX - Client error
    BAD_REQUEST(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request"),
    UNAUTHORIZED(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized"),
    PAYMENT_REQUIRED(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "Payment Required"),
    FORBIDDEN(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden"),
    NOT_FOUND(HttpURLConnection.HTTP_NOT_FOUND, "Not Found"),
    METHOD_NOT_ALLOWED(HttpURLConnection.HTTP_BAD_METHOD, "Method Not Allowed"),
    NOT_ACCEPTABLE(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(HttpURLConnection.HTTP_PROXY_AUTH, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(HttpURLConnection.HTTP_CLIENT_TIMEOUT, "Request Timeout"),
    CONFLICT(HttpURLConnection.HTTP_CONFLICT, "Conflict"),
    GONE(HttpURLConnection.HTTP_GONE, "Gone"),
    LENGTH_REQUIRED(HttpURLConnection.HTTP_LENGTH_REQUIRED, "Length Required"),
    PRECONDITION_FAILED(HttpURLConnection.HTTP_CONFLICT, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(HttpURLConnection.HTTP_ENTITY_TOO_LARGE, "Payload Too Large"),
    URI_TOO_LONG(HttpURLConnection.HTTP_REQ_TOO_LONG, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(HttpURLConnection.HTTP_UNSUPPORTED_TYPE, "Unsupported Media Type"),
    RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),
    EXPECTATION_FAILED(417, "Expectation Failed"),
    IM_A_TEAPOT(418, "I'm a teapot"),
    MISDIRECTED_REQUEST(421, "Misdirected Request"),
    UNPROCESSABLE_CONTENT(422, "Unprocessable Content"),
    LOCKED(423, "Locked"),
    FAILED_DEPENDENCY(424, "Failed Dependency"),
    TOO_EARLY(425, "Too Early"),
    UPGRADE_REQUIRED(426, "Upgrade Required"),
    PRECONDITION_REQUIRED(428, "Precondition Required"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),

    // 5XX - Server error
    INTERNAL_SERVER_ERROR(HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error"),
    NOT_IMPLEMENTED(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "Not Implemented"),
    BAD_GATEWAY(HttpURLConnection.HTTP_BAD_GATEWAY, "Bad Gateway"),
    SERVICE_UNAVAILABLE(HttpURLConnection.HTTP_UNAVAILABLE, "Service Unavailable"),
    GATEWAY_TIMEOUT(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "Gateway Timeout"),
    VERSION_NOT_SUPPORTED(HttpURLConnection.HTTP_VERSION, "HTTP Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    LOOP_DETECTED(508, "Loop Detected"),
    NOT_EXTENDED(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

    private final int code;
    private final String message;

    HttpStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public static HttpStatusCode getFromCode(int code) throws HttpException {
        for (HttpStatusCode status : values()) {
            if (status.code == code) {
                return status;
            }
        }

        throw new HttpException(String.format("Unknown HTTP status code: %d", code));
    }
}
