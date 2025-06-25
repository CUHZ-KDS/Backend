package com.moti.backend.core.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.member.domain.type.SocialType;
import com.moti.backend.core.member.infrastructure.persistence.MemberRepository;
import com.moti.backend.core.member.transfer.dto.MemberResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberResponse getCurrentMemberInfo(Member member) {
		// Guest와 일반 사용자 구분하여 응답
		if (member.getSocialType() == SocialType.GUEST) {
			return MemberResponse.fromGuest(member);
		} else {
			return MemberResponse.from(member);
		}
	}
}
