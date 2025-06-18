package com.postsmith.api.domain.login.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
	private final OAuth2User delegate;
	private final String sessionId;

	public CustomOAuth2User(OAuth2User delegate, String sessionId) {
		this.delegate = delegate;
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return delegate.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return delegate.getAuthorities();
	}

	@Override
	public String getName() {
		return delegate.getName();
	}
}