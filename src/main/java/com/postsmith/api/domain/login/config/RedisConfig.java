package com.postsmith.api.domain.login.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Value("${spring.data.redis.password}")
	private String password;

	@Bean
	RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
		config.setPassword(RedisPassword.of(password));
		return new LettuceConnectionFactory(config);
	}

	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().allowIfSubType("com.postsmith").build();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());

		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);

		template.afterPropertiesSet();
		return template;
	}
}
