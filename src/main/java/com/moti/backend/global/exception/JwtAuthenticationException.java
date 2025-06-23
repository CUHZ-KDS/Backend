package com.moti.backend.global.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
	public JwtAuthenticationException(String message) {
		super(message);
	}
}
