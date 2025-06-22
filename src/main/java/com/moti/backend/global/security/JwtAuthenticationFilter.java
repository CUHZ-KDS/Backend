package com.moti.backend.global.security;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.member.infrastructure.persistence.MemberRepository;

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
	private final MemberRepository memberRepository;
	private final TokenStorageService tokenStorageService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = extractTokenFromHeader(request);

		if (token != null) {
			try {
				if (!jwtTokenProvider.validateToken(token)) {
					sendErrorResponse(response, "C401", "유효하지 않은 인증 토큰입니다.");
					return;
				}

				Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
				String storedAccessToken = tokenStorageService.getAccessToken(memberId);

				if (storedAccessToken == null || !storedAccessToken.equals(token)) {
					sendErrorResponse(response, "C401", "유효하지 않은 인증 토큰입니다.");
					return;
				}

				// 인증 성공 처리
				Member member = memberRepository.findById(memberId).orElse(null);
				if (member != null) {
					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(member, null, Collections.emptyList());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}

			} catch (Exception e) {
				log.warn("JWT authentication failed: {}", e.getMessage());
				sendErrorResponse(response, "C401", "유효하지 않은 인증 토큰입니다.");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private void sendErrorResponse(HttpServletResponse response, String code, String message) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");

		Map<String, String> errorBody = new HashMap<>();
		errorBody.put("code", code);
		errorBody.put("message", message);

		String jsonResponse = objectMapper.writeValueAsString(errorBody);
		response.getWriter().write(jsonResponse);
		response.getWriter().flush();
	}

	private String extractTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
