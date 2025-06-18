package com.postsmith.api.domain.login.service;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.postsmith.api.domain.login.dto.CustomOidcOAuth2User;
import com.postsmith.api.domain.login.dto.GoogleOAuth2Response;
import com.postsmith.api.domain.login.dto.OAuth2Response;
import com.postsmith.api.domain.login.dto.UserCreateDto;
import com.postsmith.api.domain.login.dto.UserSessionDto;
import com.postsmith.api.entity.UsersEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOidcOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
	private final OidcUserService delegate = new OidcUserService();
	private final RedisService redisService;
	private final UserService userService;

	public static String generateSessionId() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] randomBytes = new byte[32]; // 256비트
		secureRandom.nextBytes(randomBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	}

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) {
		OidcUser oidcUser = delegate.loadUser(userRequest);
		UsersEntity.RoleEnum role = UsersEntity.RoleEnum.USER; // USER 고정값
		String tokenValue = userRequest.getAccessToken().getTokenValue();

		OAuth2Response oAuth2Response = new GoogleOAuth2Response(oidcUser.getAttributes());

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
		userSessionDto.setRole(role.name());
		userSessionDto.setUserNickname(oAuth2Response.getNickName());
		userSessionDto.setEmail(email);
		userSessionDto.setUserId(user.getId().toString());

		redisService.setValue(sessionId, userSessionDto);
		return new CustomOidcOAuth2User(oidcUser, sessionId);
	}
}
