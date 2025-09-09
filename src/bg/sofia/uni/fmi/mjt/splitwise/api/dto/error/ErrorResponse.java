package bg.sofia.uni.fmi.mjt.splitwise.api.dto.error;

import bg.sofia.uni.fmi.mjt.splitwise.api.dto.BaseResponse;
import com.google.gson.annotations.SerializedName;

public final class ErrorResponse extends BaseResponse {
    private static final String STATUS = "error";
    @SerializedName("error-type")
    private final String code;

    public ErrorResponse(String code, String message) {
        super(STATUS);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
