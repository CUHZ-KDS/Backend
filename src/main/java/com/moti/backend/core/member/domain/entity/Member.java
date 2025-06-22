package com.moti.backend.core.member.domain.entity;

import com.moti.backend.core.member.domain.type.SocialType;
import com.moti.backend.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type", nullable = false)
	private SocialType socialType;

	public static Member createGuest() {
		Member member = new Member();
		member.nickname = "guest" + generateRandomId();
		member.email = member.nickname + "@guest.temp";
		member.socialType = SocialType.GUEST;
		return member;
	}

	private static String generateRandomId() {
		return String.valueOf((int)(Math.random() * 900000) + 100000); // 6자리 숫자
	}
}
