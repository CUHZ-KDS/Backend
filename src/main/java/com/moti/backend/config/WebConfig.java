package com.moti.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.moti.backend.global.security.AuthenticationInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthenticationInterceptor authenticationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.addPathPatterns(
				"/api/v1/members/**",
				"/api/v1/auth/logout",
				"/api/v1/shows/**",
				"/api/v1/seats/**",
				"/api/v1/reservations/**"
			)
			.excludePathPatterns(
				"/api/v1/auth/guest-login",
				"/api/v1/auth/oauth-login/**"
			);
	}
}
