package com.moti.backend.global.security;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

		// CONNECT 명령어에서 인증 처리
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			return authenticateUser(accessor, message);
		}

		// CONNECT 이후 모든 메시지에서 인증 상태 확인
		if (requiresAuthentication(accessor.getCommand())) {
			return validateAuthenticatedUser(accessor, message);
		}

		return message;
	}

	private Message<?> authenticateUser(StompHeaderAccessor accessor, Message<?> message) {
		try {
			// Authorization 헤더에서 JWT 토큰 추출
			String token = extractTokenFromHeader(accessor);

			// JWT 토큰 검증
			if (!jwtTokenProvider.validateToken(token)) {
				throw new JwtAuthenticationException("유효하지 않은 토큰입니다.");
			}

			Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
			Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

			// STOMP 세션에 사용자 정보 설정
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(member, null, Collections.emptyList());

			accessor.setUser(authentication);

			return message;

		} catch (Exception e) {
			// 인증 실패 시 null 반환 (연결 차단)
			return null;
		}
	}

	private Message<?> validateAuthenticatedUser(StompHeaderAccessor accessor, Message<?> message) {
		Principal user = accessor.getUser();

		if (user == null) {
			log.warn("인증되지 않은 사용자의 메시지 시도 - command: {}", accessor.getCommand());
			return null; // 메시지 차단
		}

		// 추가 검증 로직 (필요시)
		String memberId = (String)accessor.getSessionAttributes().get("memberId");
		if (memberId == null) {
			log.warn("세션에 memberId가 없음");
			return null;
		}

		return message;
	}

	private boolean requiresAuthentication(StompCommand command) {
		return command == StompCommand.SUBSCRIBE ||
			command == StompCommand.SEND ||
			command == StompCommand.MESSAGE;
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
