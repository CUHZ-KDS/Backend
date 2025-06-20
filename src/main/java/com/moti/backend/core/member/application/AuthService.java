package com.moti.backend.core.member.application;

import org.springframework.stereotype.Service;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.member.exception.InvalidRefreshTokenException;
import com.moti.backend.core.member.exception.MemberNotFoundException;
import com.moti.backend.core.member.infrastructure.persistence.MemberRepository;
import com.moti.backend.core.member.transfer.dto.LoginResponse;
import com.moti.backend.core.member.transfer.dto.MemberResponse;
import com.moti.backend.core.member.transfer.dto.RefreshTokenResponse;
import com.moti.backend.global.security.JwtTokenProvider;
import com.moti.backend.global.security.TokenStorageService;
import com.moti.backend.global.validation.RefreshTokenValidator;

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
	private final RefreshTokenValidator refreshTokenValidator;

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

	public RefreshTokenResponse refreshToken(String refreshToken){
		Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);

		refreshTokenValidator.validateRefreshToken(refreshToken, memberId);

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

		String newAccessToken = jwtTokenProvider.generateAccessToken(member);
		String newRefreshToken = jwtTokenProvider.generateRefreshToken(member);

		tokenStorageService.storeTokens(memberId, newAccessToken, newRefreshToken);
		log.info("토큰이 성공적으로 재발급되었습니다. 회원 ID: {}", memberId);

		return RefreshTokenResponse.builder()
			.accessToken(newAccessToken)
			.refreshToken(newRefreshToken)
			.member(MemberResponse.from(member))
			.build();
	}
}
