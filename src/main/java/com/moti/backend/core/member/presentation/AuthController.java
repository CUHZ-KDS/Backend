package com.moti.backend.core.member.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moti.backend.core.member.application.AuthService;
import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.member.transfer.dto.LoginResponse;
import com.moti.backend.core.member.transfer.dto.RefreshTokenRequest;
import com.moti.backend.core.member.transfer.dto.RefreshTokenResponse;
import com.moti.backend.global.dto.response.ApiResponse;

import jakarta.validation.Valid;
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

	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
		@Valid @RequestBody RefreshTokenRequest request) {
		RefreshTokenResponse refreshTokenResponse = authService.refreshToken(request.getRefreshToken());
		return ResponseEntity.ok(ApiResponse.success(refreshTokenResponse));
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout() {
		authService.logout();
		return ResponseEntity.ok(ApiResponse.success(null, "로그아웃이 완료되었습니다."));
	}
}