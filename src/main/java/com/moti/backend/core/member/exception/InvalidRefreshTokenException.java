package com.moti.backend.core.member.exception;

import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;

public class InvalidRefreshTokenException extends BusinessException {
	public InvalidRefreshTokenException(String message) {
		super(StatusCode.BAD_REQUEST, message);
	}
}
