package com.moti.backend.config;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {
	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Value("${spring.data.redis.password:}")
	private String password;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);

		if (!password.isEmpty()) {
			factory.setPassword(password);
		}

		return factory;
	}

	// public RedisTemplate<String, String> redisTemplate() {
	// 	RedisTemplate<String, String> template = new RedisTemplate<>();
	// 	template.setConnectionFactory(redisConnectionFactory());
	//
	// 	template.setKeySerializer(new StringRedisSerializer());
	// 	template.setValueSerializer(new StringRedisSerializer());
	// 	return template;
	// }

	@Bean
	public GenericJackson2JsonRedisSerializer jsonRedisSerializer(ObjectMapper objectMapper) {
		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}

	/**
	 * seat:{show-scheduleId}:select-member 데이터를 위한 템플릿
	 * Value Type: Map<String, Set<String>>
	 */
	@Bean(name = "redisTemplateForSelectMember")
	public RedisTemplate<String, Map<String, Set<String>>> redisTemplateForSelectMember(
		RedisConnectionFactory connectionFactory,
		GenericJackson2JsonRedisSerializer jsonRedisSerializer) {

		RedisTemplate<String, Map<String, Set<String>>> template = new RedisTemplate<>();
		configureTemplate(template, connectionFactory, jsonRedisSerializer);
		return template;
	}

	/**
	 * seat:{show-scheduleId}:status 데이터를 위한 템플릿
	 * Value Type: Map<String, String>
	 */
	@Bean(name = "redisTemplateForSeatStatus")
	public RedisTemplate<String, Map<String, String>> redisTemplateForSeatStatus(
		RedisConnectionFactory connectionFactory,
		GenericJackson2JsonRedisSerializer jsonRedisSerializer) {

		RedisTemplate<String, Map<String, String>> template = new RedisTemplate<>();
		configureTemplate(template, connectionFactory, jsonRedisSerializer);
		return template;
	}

	/**
	 * hold:{show-scheduleId} 와 같이 간단한 Key-Value 데이터를 위한 템플릿
	 * Value Type: String
	 */
	// @Bean(name = "redisTemplateForHold")
	// public RedisTemplate<String, String> redisTemplateForHold(RedisConnectionFactory connectionFactory) {
	// 	RedisTemplate<String, String> template = new RedisTemplate<>();
	// 	template.setConnectionFactory(connectionFactory);
	// 	template.setKeySerializer(new StringRedisSerializer());
	// 	template.setValueSerializer(new StringRedisSerializer()); // String이므로 JSON 직렬화 불필요
	// 	return template;
	// }

	// --- 중복 설정을 위한 헬퍼 메서드 ---
	private <T> void configureTemplate(RedisTemplate<String, T> template,
		RedisConnectionFactory connectionFactory,
		GenericJackson2JsonRedisSerializer jsonRedisSerializer) {
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(jsonRedisSerializer);
		template.setHashValueSerializer(jsonRedisSerializer);
		template.afterPropertiesSet();
	}
}
