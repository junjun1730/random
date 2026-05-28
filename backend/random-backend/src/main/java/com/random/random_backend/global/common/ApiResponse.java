package com.random.random_backend.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final String status;
    private final T data;
    private final String message;

    public ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>("SUCCESS", data, null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>("SUCCESS", null, null);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>("ERROR", null, message);
    }
}
