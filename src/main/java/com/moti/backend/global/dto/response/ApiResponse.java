package com.moti.backend.global.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.moti.backend.global.type.StatusCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private final String code;
    private final String message;
    private final T data;
    private final List<FieldError> errors;
    private final LocalDateTime timestamp;

    // 성공 응답
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(data)
                .errors(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 성공 응답 (메시지 지정)
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(StatusCode.OK.getCode())
                .message(message)
                .data(data)
                .errors(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String code, String message, List<FieldError> errors) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 실패 응답 (StatusCode 사용)
    public static <T> ApiResponse<T> error(StatusCode statusCode, List<FieldError> errors) {
        return ApiResponse.<T>builder()
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .data(null)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Getter
    @Builder
    public static class FieldError {
        private final String field;   // 필드명
        private final String value;   // 잘못된 값
        private final String reason;  // 에러 이유
    }
}
