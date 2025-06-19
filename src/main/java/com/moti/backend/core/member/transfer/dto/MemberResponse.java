package com.moti.backend.core.member.transfer.dto;

import com.moti.backend.core.member.domain.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {
	private Long id;
	private String email;
	private String nickname;
	private String memberType;

	public static MemberResponse from(Member member) {
		return new MemberResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getSocialType().name()
		);
	}

	// Guest용 생성자 (email 제외)
	public static MemberResponse fromGuest(Member member) {
		return new MemberResponse(
			member.getId(),
			null, // Guest는 email 노출 안함
			member.getNickname(),
			member.getSocialType().name()
		);
	}
}
