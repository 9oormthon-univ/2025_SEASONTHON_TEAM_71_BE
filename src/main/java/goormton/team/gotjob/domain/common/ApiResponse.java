package goormton.team.gotjob.domain.common;

public record ApiResponse<T>(String message, T data) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>("OK", data);
    }
}