package com.moti.backend.core.show.exception;

import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;

public class ShowScheduleNotFoundException extends BusinessException {
	public ShowScheduleNotFoundException(String message) {
		super(StatusCode.NOT_FOUND, message);
	}
}
