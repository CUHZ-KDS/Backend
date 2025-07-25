package com.moti.backend.global.security;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.member.exception.MemberNotFoundException;
import com.moti.backend.core.member.infrastructure.persistence.MemberRepository;
import com.moti.backend.global.exception.JwtAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		StompCommand command = accessor.getCommand();

		// CONNECT 명령어에서 인증 처리
		if (StompCommand.CONNECT.equals(command)) {
			return authenticateUser(accessor, message);
		}

		return message;
	}

	private Message<?> authenticateUser(StompHeaderAccessor accessor, Message<?> message) {
		try {
			String token = extractTokenFromHeader(accessor);
			log.info("WebSocket CONNECT 요청 - 세션: {}", accessor.getSessionId());

			if (!jwtTokenProvider.validateToken(token)) {
				log.warn("유효하지 않은 토큰으로 CONNECT 시도 - 세션: {}", accessor.getSessionId());
				throw new JwtAuthenticationException("유효하지 않은 토큰입니다.");
			}

			Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
			Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

			// WebSocket 세션에 저장
			accessor.getSessionAttributes().put("user", member);
			
			return message;
		} catch (Exception e) {
			log.error("WebSocket 인증 실패 - 세션: {}, 오류: {}", accessor.getSessionId(), e.getMessage());
			return null;
		}
	}

	private String extractTokenFromHeader(StompHeaderAccessor accessor) {
		List<String> authHeaders = accessor.getNativeHeader("Authorization");
		if (authHeaders != null && !authHeaders.isEmpty()) {
			String authHeader = authHeaders.get(0);
			if (authHeader.startsWith("Bearer ")) {
				return authHeader.substring(7);
			}
		}
		throw new JwtAuthenticationException("Authorization header not found");
	}
}
