package com.moti.backend.core.member.application;

import org.springframework.stereotype.Service;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.member.infrastructure.persistence.MemberRepository;
import com.moti.backend.core.member.transfer.dto.LoginResponse;
import com.moti.backend.core.member.transfer.dto.MemberResponse;
import com.moti.backend.global.security.JwtTokenProvider;
import com.moti.backend.global.security.TokenStorageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenStorageService tokenStorageService;

	@Transactional
	public LoginResponse guestLogin() {
		Member guestMember = Member.createGuest();
		Member savedMember = memberRepository.save(guestMember);

		String accessToken = jwtTokenProvider.generateAccessToken(savedMember);
		String refreshToken = jwtTokenProvider.generateRefreshToken(savedMember);

		tokenStorageService.storeTokens(savedMember.getId(), accessToken, refreshToken);

		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.member(MemberResponse.fromGuest(savedMember))
			.build();
	}
}
