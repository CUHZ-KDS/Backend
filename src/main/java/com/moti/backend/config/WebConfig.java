package com.moti.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.moti.backend.global.security.MemberLoadingInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final MemberLoadingInterceptor memberLoadingInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(memberLoadingInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/api/v1/auth/guest-login", "/api/v1/auth/refresh", "/api/v1/auth/oauth-login/**",
				"/api/v1/shows/**");
	}
}