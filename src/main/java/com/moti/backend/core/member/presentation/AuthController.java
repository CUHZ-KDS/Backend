package com.moti.backend.core.member.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moti.backend.core.member.application.AuthService;
import com.moti.backend.core.member.transfer.dto.LoginResponse;
import com.moti.backend.global.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;

	@PostMapping("/guest-login")
	public ResponseEntity<ApiResponse<LoginResponse>> guestLogin() {
		LoginResponse loginResponse = authService.guestLogin();
		return ResponseEntity.ok(ApiResponse.success(loginResponse));
	}
}