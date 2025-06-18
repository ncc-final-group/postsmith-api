package com.postsmith.api.domain.login.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.postsmith.api.domain.login.dto.UserSessionDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	public void setValue(String key, UserSessionDto value) {
		redisTemplate.opsForValue().set(key, value, Duration.ofHours(1));
	}

	public UserSessionDto getValue(String key) {
		Object value = redisTemplate.opsForValue().get(key);
		return (value instanceof UserSessionDto) ? (UserSessionDto) value : null;
	}

	public void deleteValue(String key) {
		redisTemplate.delete(key);
	}
}
