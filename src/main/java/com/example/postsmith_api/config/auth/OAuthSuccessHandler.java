package com.example.postsmith_api.config.auth;

import com.example.postsmith_api.config.auth.dto.CustomOAuth2User;
import com.example.postsmith_api.config.auth.dto.UserSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final RedisTemplate<String, UserSession> redisTemplate;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();
        CustomOAuth2User customUser = (CustomOAuth2User) oauthUser;
        String provider = customUser.getProvider();
        String email = null;
        String name = null;
        if(provider.equals("google")){
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
        }else if(provider.equals("kakao")) {
            HashMap<String, Object> account = oauthUser.getAttribute("kakao_account");
            email = (String) account.get("email");
            name = "test";
        }else if(provider.equals("naver")) {
            HashMap<String, Object> authResponse = oauthUser.getAttribute("response");
            email = (String) authResponse.get("email");
            name = (String) authResponse.get("name");
        }
        log.info("🎉 OAuth2 로그인 성공: name={}, email={}", name, email);

        // 세션 ID 대신 직접 생성하는 키 (예: UUID)
        String sessionId = UUID.randomUUID().toString();
        // UserSession 같은 DTO로 감싸서 저장 (원하는 데이터 넣기)
        UserSession userSession = new UserSession(email);
        // Redis에 저장 (예: key: session:<sessionId>)
        redisTemplate.opsForValue().set("session:" + provider +"_"+ sessionId, userSession, 1, TimeUnit.HOURS);

        // 클라이언트에 세션 ID 쿠키로 전달 (Secure, HttpOnly 설정 필수)
        Cookie sessionCookie = new Cookie("SESSION_ID", provider + "_" + sessionId);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(3600);
        response.addCookie(sessionCookie);

        // 로그인 후 리디렉트할 URL
        response.sendRedirect("http://localhost:8090/api1/home");
    }
}
