package com.moti.backend.core.reservation.exception;

import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;

public class ShowSeatNotFoundException extends BusinessException {
    public ShowSeatNotFoundException(String message) {
        super(StatusCode.NOT_FOUND, message);
    }
}
