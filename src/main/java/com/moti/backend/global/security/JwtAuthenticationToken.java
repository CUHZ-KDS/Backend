package com.moti.backend.global.security;

import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.Getter;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final Long memberId;
	private final String token;

	public JwtAuthenticationToken(Long memberId, String token) {
		super(Collections.emptyList());
		this.memberId = memberId;
		this.token = token;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return memberId;
	}
}
