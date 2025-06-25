package com.moti.backend.core.reservation.exception;

import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;

public class InvalidReserveStateException extends BusinessException {
    public InvalidReserveStateException(String message) {
        super(StatusCode.CONFLICT, message);
    }
}
