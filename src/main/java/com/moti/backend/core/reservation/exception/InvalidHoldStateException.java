package com.moti.backend.core.reservation.exception;

import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;

public class InvalidHoldStateException extends BusinessException {
    public InvalidHoldStateException(String message) {
        super(StatusCode.CONFLICT, message);
    }
}
