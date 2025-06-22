package com.moti.backend.core.member.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moti.backend.core.member.application.MemberService;
import com.moti.backend.core.member.transfer.dto.MemberResponse;
import com.moti.backend.global.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<MemberResponse>> getMyInfo() {
		MemberResponse memberResponse = memberService.getCurrentMemberInfo();
		return ResponseEntity.ok(ApiResponse.success(memberResponse));
	}
}