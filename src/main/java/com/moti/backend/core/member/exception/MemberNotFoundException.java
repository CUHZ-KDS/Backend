package com.moti.backend.core.member.exception;

import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;

public class MemberNotFoundException extends BusinessException {
	public MemberNotFoundException(String message) {
		super(StatusCode.NOT_FOUND, message);
	}
}
