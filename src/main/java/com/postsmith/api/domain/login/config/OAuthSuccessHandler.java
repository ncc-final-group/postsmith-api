package com.postsmith.api.domain.login.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.postsmith.api.domain.login.dto.CustomOAuth2User;
import com.postsmith.api.domain.login.dto.CustomOidcOAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
	@Value("${url.base}")
	private String baseUrl;
	
	@Value("${url.domain}")
	private String domain;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		String sessionId = null;
		if (authentication.getPrincipal() instanceof CustomOAuth2User customUser) {
			log.info("Custom OAuth2 User Session ID: " + customUser.getAttributes());
			sessionId = customUser.getSessionId();
		}
		if (authentication.getPrincipal() instanceof CustomOidcOAuth2User oidcUser) {
			log.info("Custom OIDC OAuth2 User Session ID: " + oidcUser.getAttributes());
			sessionId = oidcUser.getSessionId();
		}
//		Cookie cookie = new Cookie("CLIENT_SESSION_ID", sessionId);
//		cookie.setPath("/");
//		cookie.setHttpOnly(true);
//		cookie.setSecure(true);
//		cookie.setMaxAge(3600);
//		cookie.setDomain("postsmith.kro.kr");
//		response.addCookie(cookie);
		ResponseCookie responseCookie = ResponseCookie.from("CLIENT_SESSION_ID", sessionId).path("/").httpOnly(true).secure(true).domain("." + domain).sameSite("None").build();
		response.addHeader("Set-Cookie", responseCookie.toString());
		log.info("OAuth2 Authentication Success: " + authentication.getName());

		response.sendRedirect(baseUrl);
	}

}
