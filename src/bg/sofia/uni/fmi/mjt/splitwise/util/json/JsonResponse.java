package bg.sofia.uni.fmi.mjt.splitwise.util.json;

public record JsonResponse<T>(String status, T data) {
}