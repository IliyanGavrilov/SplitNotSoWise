package bg.sofia.uni.fmi.mjt.splitwise.api.dto;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    private static final String SUCCESS = "success";
    @SerializedName("result")
    private final String status;

    public BaseResponse(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return SUCCESS.equals(status);
    }
}
