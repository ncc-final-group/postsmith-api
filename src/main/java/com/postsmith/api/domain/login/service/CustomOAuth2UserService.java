package com.postsmith.api.domain.login.service;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.postsmith.api.domain.login.dto.CustomOAuth2User;
import com.postsmith.api.domain.login.dto.GithubOAuth2Response;
import com.postsmith.api.domain.login.dto.KakaoOAuth2Response;
import com.postsmith.api.domain.login.dto.NaverOAuth2Response;
import com.postsmith.api.domain.login.dto.OAuth2Response;
import com.postsmith.api.domain.login.dto.UserCreateDto;
import com.postsmith.api.domain.login.dto.UserSessionDto;
import com.postsmith.api.entity.UsersEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final RedisService redisService;
	private final UserService userService;

	public static String generateSessionId() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] randomBytes = new byte[32]; // 256비트
		secureRandom.nextBytes(randomBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		UsersEntity.RoleEnum role = UsersEntity.RoleEnum.USER; // USER 고정값
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String tokenValue = userRequest.getAccessToken().getTokenValue();
		OAuth2Response oAuth2Response = null;

		switch (registrationId) {
		case "github" -> oAuth2Response = new GithubOAuth2Response(oAuth2User.getAttributes());
		case "naver" -> oAuth2Response = new NaverOAuth2Response(oAuth2User.getAttributes());
		case "kakao" -> oAuth2Response = new KakaoOAuth2Response(oAuth2User.getAttributes());
		default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
		}

		String sessionId = generateSessionId();
		String email = oAuth2Response.getEmail();
		if (email == null || email.isEmpty()) {
			throw new OAuth2AuthenticationException("Email not found in OAuth2 response");
		}
		UsersEntity.ProviderEnum provider = UsersEntity.ProviderEnum.valueOf(oAuth2Response.getProvider().toUpperCase());

		UsersEntity user = userService.readUserByEmail(email, provider);
		if (user == null) {
			userService.createUser(new UserCreateDto(email, oAuth2Response.getProvider(), role.name(), oAuth2Response.getNickName()));
			user = userService.readUserByEmail(email, provider);
		}

		UserSessionDto userSessionDto = new UserSessionDto();
		userSessionDto.setAccessToken(tokenValue);
		userSessionDto.setUserId(user.getId().toString());
		userSessionDto.setEmail(email);
		userSessionDto.setRole(role.name());
		userSessionDto.setUserNickname(oAuth2Response.getNickName());

		redisService.setValue(sessionId, userSessionDto);
		return new CustomOAuth2User(oAuth2User, sessionId);
	}

}
