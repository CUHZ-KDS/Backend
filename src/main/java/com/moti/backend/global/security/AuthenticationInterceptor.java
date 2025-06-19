package com.moti.backend.global.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moti.backend.global.dto.response.ErrorResponse;
import com.moti.backend.global.type.StatusCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// OPTIONS 요청은 통과
		if ("OPTIONS".equals(request.getMethod())) {
			return true;
		}

		String token = extractTokenFromHeader(request);

		if (token == null) {
			handleUnauthorized(response, "유효하지 않은 인증 토큰입니다.");
			return false;
		}

		if (!jwtTokenProvider.validateToken(token)) {
			handleUnauthorized(response, "유효하지 않은 인증 토큰입니다.");
			return false;
		}

		if (!jwtTokenProvider.isAccessToken(token)) {
			handleUnauthorized(response, "유효하지 않은 인증 토큰입니다.");
			return false;
		}

		// 토큰에서 사용자 정보 추출하여 request에 저장
		Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
		String memberType = jwtTokenProvider.getMemberTypeFromToken(token);
		String nickname = jwtTokenProvider.getNicknameFromToken(token);

		request.setAttribute("memberId", memberId);
		request.setAttribute("memberType", memberType);
		request.setAttribute("nickname", nickname);

		return true;
	}

	private String extractTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");

		ErrorResponse errorResponse = ErrorResponse.of(StatusCode.UNAUTHORIZED, message);
		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
