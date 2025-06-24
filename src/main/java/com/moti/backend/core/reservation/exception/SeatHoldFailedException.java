package com.moti.backend.core.reservation.exception;

import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;

public class SeatHoldFailedException extends BusinessException {
    public SeatHoldFailedException(String message) {
        super(StatusCode.LOCK_FAILED, message);
    }
}
