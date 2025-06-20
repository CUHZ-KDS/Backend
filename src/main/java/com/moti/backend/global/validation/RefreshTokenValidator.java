package com.moti.backend.global.validation;

import org.springframework.stereotype.Component;

import com.moti.backend.global.exception.InvalidRefreshTokenException;
import com.moti.backend.global.security.JwtTokenProvider;
import com.moti.backend.global.security.TokenStorageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenValidator {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenStorageService tokenStorageService;

	public void validateRefreshToken(String refreshToken, Long memberId) {
		if (!jwtTokenProvider.validateToken(refreshToken)) {
			throw new InvalidRefreshTokenException("유효하지 않거나 만료된 Refresh Token입니다.");
		}

		if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
			throw new InvalidRefreshTokenException("올바른 Refresh Token이 아닙니다.");
		}

		String storedRefreshToken = tokenStorageService.getRefreshToken(memberId);
		if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
			throw new InvalidRefreshTokenException("유효하지 않은 Refresh Token입니다.");
		}
	}
}
