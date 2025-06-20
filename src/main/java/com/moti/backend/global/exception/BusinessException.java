package com.moti.backend.global.exception;

import com.moti.backend.global.type.StatusCode;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final StatusCode statusCode;

	public BusinessException(StatusCode statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}
}