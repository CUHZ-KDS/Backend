package com.moti.backend.global.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.moti.backend.core.member.domain.entity.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	private final Key secretKey;
	private final long accessTokenExpiration;
	private final long refreshTokenExpiration;

	public JwtTokenProvider(
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.access-token-expiration}") long accessTokenExpiration,
		@Value("${jwt.refresh-expiration}") long refreshTokenExpiration) {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
		this.accessTokenExpiration = accessTokenExpiration;
		this.refreshTokenExpiration = refreshTokenExpiration;
	}

	public String generateAccessToken(Member member) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + accessTokenExpiration);

		return Jwts.builder()
			.setSubject(member.getId().toString())
			.claim("memberType", member.getSocialType().name())
			.claim("nickname", member.getNickname())
			.claim("tokenType", "ACCESS")
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	public String generateRefreshToken(Member member) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + refreshTokenExpiration);

		return Jwts.builder()
			.setSubject(member.getId().toString())
			.claim("tokenType", "REFRESH")
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	public Long getMemberIdFromToken(String token) {
		Claims claims = parseClaims(token);
		return Long.parseLong(claims.getSubject());
	}

	public String getMemberTypeFromToken(String token) {
		Claims claims = parseClaims(token);
		return claims.get("memberType", String.class);
	}

	public String getNicknameFromToken(String token) {
		Claims claims = parseClaims(token);
		return claims.get("nickname", String.class);
	}

	public String getTokenTypeFromToken(String token) {
		Claims claims = parseClaims(token);
		return claims.get("tokenType", String.class);
	}

	public boolean validateToken(String token) {
		try {
			Claims claims = parseClaims(token);
			return !claims.getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException e) {
			log.warn("유효하지 않은 JWT 토큰입니다: {}", e.getMessage());
			return false;
		}
	}

	public boolean isAccessToken(String token) {
		try {
			String tokenType = getTokenTypeFromToken(token);
			return "ACCESS".equals(tokenType);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isRefreshToken(String token) {
		try {
			String tokenType = getTokenTypeFromToken(token);
			return "REFRESH".equals(tokenType);
		} catch (Exception e) {
			return false;
		}
	}

	public long getExpirationTime(String token) {
		Claims claims = parseClaims(token);
		return claims.getExpiration().getTime();
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
