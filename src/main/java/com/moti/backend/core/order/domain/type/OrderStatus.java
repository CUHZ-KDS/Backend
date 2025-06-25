package com.moti.backend.core.order.domain.type;

public enum OrderStatus {
    CREATED,     // 주문 생성됨 (결제 전)
    PAID,        // 결제 완료됨
    FAILED,      // 결제 실패
    CANCELLED,   // 주문 취소됨 (결제 실패, 유저 취소 등)
    EXPIRED      // 결제 유효 시간 초과로 만료됨
}