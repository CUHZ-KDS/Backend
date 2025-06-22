package com.moti.backend.global.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.global.exception.UnauthorizedException;

@Component
public class SecurityUtils {

	public static Member getCurrentMember() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof Member) {
			return (Member) authentication.getPrincipal();
		}
		throw new UnauthorizedException("인증되지 않은 사용자입니다.");
	}

	public static boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated() &&
			!(authentication instanceof AnonymousAuthenticationToken);
	}
}