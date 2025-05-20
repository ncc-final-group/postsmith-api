package com.example.postsmith_api.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String URI = "/auth/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 login success");

        // OAuth2User에서 email 추출
        OAuth2User oAuthUser = (OAuth2User) authentication.getPrincipal();
        String email = oAuthUser.getAttribute("email");  // 또는 userId로 사용하는 다른 값

        // 세션에 userId(email)만 저장
        request.getSession().setAttribute("userId", email);
        log.info("Session userId set to: {}", email);

        // 리다이렉트 URL 설정
        setDefaultTargetUrl(URI);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
