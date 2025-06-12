package com.moti.backend.global.exception;

import com.moti.backend.global.dto.response.ErrorResponse;
import com.moti.backend.global.type.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("잘못된 JSON 요청: {}", ex.getMessage());
        return buildErrorResponse(StatusCode.BAD_REQUEST, "요청 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("유효성 검사 실패: {}", ex.getBindingResult());
        return buildErrorResponse(StatusCode.INVALID_INPUT, "입력값이 유효하지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("파라미터 '%s' 값 '%s'는 허용되지 않는 타입입니다.", ex.getName(), ex.getValue());
        log.warn("타입 불일치: {}", message);
        return buildErrorResponse(StatusCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("예상치 못한 서버 에러 발생", ex);
        return buildErrorResponse(StatusCode.INTERNAL_ERROR);
    }

    // 공통 응답 생성기
    private ResponseEntity<ErrorResponse> buildErrorResponse(StatusCode statusCode) {
        return ResponseEntity.status(statusCode.getStatus()).body(ErrorResponse.of(statusCode));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(StatusCode statusCode, String customMessage) {
        return ResponseEntity.status(statusCode.getStatus()).body(ErrorResponse.of(statusCode, customMessage));
    }
}
