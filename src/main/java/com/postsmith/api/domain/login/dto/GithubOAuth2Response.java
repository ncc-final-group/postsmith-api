package com.postsmith.api.domain.login.dto;

import java.util.Map;

public class GithubOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attributes;

	public GithubOAuth2Response(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProvider() {
		return "github";
	}

	@Override
	public String getProviderId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getEmail() {
		Object email = attributes.get("email");
		if (email != null) {
			return attributes.get("email").toString();
		} else {
			return "";
		}
	}

	@Override
	public String getNickName() {
		return attributes.get("login").toString();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
