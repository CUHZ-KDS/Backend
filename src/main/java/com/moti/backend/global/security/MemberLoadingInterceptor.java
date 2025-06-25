package com.moti.backend.global.security;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.member.exception.MemberNotFoundException;
import com.moti.backend.core.member.infrastructure.persistence.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberLoadingInterceptor implements HandlerInterceptor {

	private final MemberRepository memberRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof JwtAuthenticationToken) {
			JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken)authentication;
			Long memberId = jwtAuth.getMemberId();

			Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

			UsernamePasswordAuthenticationToken newAuth =
				new UsernamePasswordAuthenticationToken(member, null, Collections.emptyList());
			SecurityContextHolder.getContext().setAuthentication(newAuth);
		}
		return true;
	}
}
