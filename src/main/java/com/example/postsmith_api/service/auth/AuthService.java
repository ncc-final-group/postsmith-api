package com.example.postsmith_api.service.auth;

import com.example.postsmith_api.domain.User;
import com.example.postsmith_api.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public Cookie login(String email){
        // 로그인 로직 구현
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        log.info("login user: {}", user.getEmail());
        // 사용자 인증 및 세션 생성
        String sessionId = new StandardSessionIdGenerator().generateSessionId();
        String sessionKey = "SESSION_ID:" + sessionId;
        log.info("sessionKey: {}", sessionKey);
        redisTemplate.opsForValue().set(sessionKey, user.getId());
        log.info("redis insert success: {}", sessionKey);
        // 로그인 성공 시 세션 ID 또는 토큰을 반환합니다.
        Cookie cookie = new Cookie("SESSION_ID", sessionId);
        cookie.setMaxAge(60 * 60); // 1시간
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setHttpOnly(true); // JavaScript에서 접근 불가
        return cookie;
    }
}
