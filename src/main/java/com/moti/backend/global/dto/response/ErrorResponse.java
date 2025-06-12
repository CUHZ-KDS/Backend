package com.moti.backend.global.dto.response;

import com.moti.backend.global.type.StatusCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final String code;
    private final String message;

    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

    public static ErrorResponse of(StatusCode statusCode) {
        return ErrorResponse.builder()
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .build();
    }

    public static ErrorResponse of(StatusCode statusCode, String customMessage) {
        return ErrorResponse.builder()
                .code(statusCode.getCode())
                .message(customMessage)
                .build();
    }
}
