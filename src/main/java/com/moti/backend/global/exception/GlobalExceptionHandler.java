package com.moti.backend.global.exception;

import java.util.List;
import java.util.stream.Collectors;

import com.moti.backend.global.dto.response.ApiResponse;
import com.moti.backend.global.type.StatusCode;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // JSON 파싱/타입 변환 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("Message not readable exception occurred", ex);

        return ResponseEntity
                .status(StatusCode.BAD_REQUEST.getStatus())
                .body(ApiResponse.error(StatusCode.BAD_REQUEST, List.of()));
    }

    // Bean Validation 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("Validation error occurred: {}", ex.getBindingResult());

        List<ApiResponse.FieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ApiResponse.FieldError.builder()
                        .field(error.getField())
                        //.value(String.valueOf(error.getRejectedValue())) // 보안상 제외 가능
                        .reason(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(StatusCode.INVALID_INPUT.getStatus())
                .body(ApiResponse.error(StatusCode.INVALID_INPUT, errors));
    }

    // 타입 변환 실패 (예: int 파라미터에 문자 입력 등)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("TypeMismatchException caught: {}", ex.getMessage());

        String message = String.format("DTO 파라미터 값 '%s'는 허용되지 않는 값입니다.", ex.getValue());
        return ResponseEntity
                .status(StatusCode.BAD_REQUEST.getStatus())
                .body(ApiResponse.error(StatusCode.BAD_REQUEST.getCode(), message, List.of()));
    }

    // 알 수 없는 예외
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        return ResponseEntity
                .status(StatusCode.INTERNAL_ERROR.getStatus())
                .body(ApiResponse.error(StatusCode.INTERNAL_ERROR, List.of()));
    }
}