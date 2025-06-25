package com.moti.backend.global.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		Map<String, String> errorBody = new HashMap<>();
		errorBody.put("code", "C401");
		errorBody.put("message", "유효하지 않은 인증 토큰입니다.");

		String jsonResponse = objectMapper.writeValueAsString(errorBody);
		response.getWriter().write(jsonResponse);
		response.getWriter().flush();
	}
}
