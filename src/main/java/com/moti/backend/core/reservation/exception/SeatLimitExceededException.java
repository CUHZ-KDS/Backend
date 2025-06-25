package com.moti.backend.core.reservation.exception;

import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;

public class SeatLimitExceededException extends BusinessException {
    public SeatLimitExceededException(String message) {
        super(StatusCode.BAD_REQUEST, message);
    }
}
