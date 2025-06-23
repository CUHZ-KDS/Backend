package com.moti.backend.global.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.moti.backend.global.exception.JwtAuthenticationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenStorageService tokenStorageService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = extractTokenFromHeader(request);

		if (token != null) {
			try {
				if (!jwtTokenProvider.validateToken(token)) {
					throw new JwtAuthenticationException("유효하지 않은 토큰입니다.");
				}

				Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
				String storedAccessToken = tokenStorageService.getAccessToken(memberId);

				if (storedAccessToken == null || !storedAccessToken.equals(token)) {
					throw new JwtAuthenticationException("유효하지 않은 토큰입니다.");
				}

				// 커스텀한 Authentication 객체 생성 후 SecurityContext에 저장
				JwtAuthenticationToken authentication = new JwtAuthenticationToken(memberId, token);
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (JwtAuthenticationException e) {
				log.warn("JWT 인증에 실패했습니다: {}", e.getMessage());
				SecurityContextHolder.clearContext();
				throw e;
			}
		}

		filterChain.doFilter(request, response);
	}

	private String extractTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
