package com.moti.backend.global.security;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenStorageService {

	private final RedisTemplate<String, String> redisTemplate;
	private final JwtTokenProvider jwtTokenProvider;

	private static final String ACCESS_TOKEN_PREFIX = "access_token:";
	private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

	public void storeTokens(Long memberId, String accessToken, String refreshToken) {
		storeAccessToken(memberId, accessToken);
		storeRefreshToken(memberId, refreshToken);
	}

	public void storeAccessToken(Long memberId, String accessToken) {
		String key = ACCESS_TOKEN_PREFIX + memberId;
		long expiration = jwtTokenProvider.getExpirationTime(accessToken);
		long ttl = (expiration - System.currentTimeMillis()) / 1000; // 초 단위

		if (ttl > 0) {
			redisTemplate.opsForValue().set(key, accessToken, Duration.ofSeconds(ttl));
			log.info("Access 토큰이 저장되었습니다. member: {}", memberId);
		}
	}

	public void storeRefreshToken(Long memberId, String refreshToken) {
		String key = REFRESH_TOKEN_PREFIX + memberId;
		long expiration = jwtTokenProvider.getExpirationTime(refreshToken);
		long ttl = (expiration - System.currentTimeMillis()) / 1000; // 초 단위

		if (ttl > 0) {
			redisTemplate.opsForValue().set(key, refreshToken, Duration.ofSeconds(ttl));
			log.info("Refresh 토큰이 저장되었습니다. member: {}", memberId);
		}
	}

	public String getAccessToken(Long memberId) {
		String key = ACCESS_TOKEN_PREFIX + memberId;
		return redisTemplate.opsForValue().get(key);
	}

	public String getRefreshToken(Long memberId) {
		String key = REFRESH_TOKEN_PREFIX + memberId;
		return redisTemplate.opsForValue().get(key);
	}

	public void removeTokens(Long memberId) {
		String accessKey = ACCESS_TOKEN_PREFIX + memberId;
		String refreshKey = REFRESH_TOKEN_PREFIX + memberId;

		redisTemplate.delete(accessKey);
		redisTemplate.delete(refreshKey);
		log.info("모든 token 정보가 삭제되었습니다. member: {}", memberId);
	}

	public boolean hasValidAccessToken(Long memberId) {
		String token = getAccessToken(memberId);
		return token != null && jwtTokenProvider.validateToken(token);
	}

	public boolean hasValidRefreshToken(Long memberId) {
		String token = getRefreshToken(memberId);
		return token != null && jwtTokenProvider.validateToken(token);
	}
}
