package com.moti.backend.global.exception;

import com.moti.backend.global.type.StatusCode;

public class UnauthorizedException extends BusinessException {
	public UnauthorizedException(String message) {
		super(StatusCode.UNAUTHORIZED, message);
	}
}
