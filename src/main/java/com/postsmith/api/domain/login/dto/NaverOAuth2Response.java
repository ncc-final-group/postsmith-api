package com.postsmith.api.domain.login.dto;

import java.util.Map;

public class NaverOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attributes;

	public NaverOAuth2Response(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getProviderId() {
		Object props = attributes.get("response");
		if (props instanceof Map<?, ?> map) {
			Object id = map.get("id");
			return id != null ? id.toString() : null;
		}
		return null;
	}

	@Override
	public String getEmail() {
		Object props = attributes.get("response");
		if (props instanceof Map<?, ?> map) {
			Object email = map.get("email");
			return email != null ? email.toString() : null;
		}
		return null;
	}

	@Override
	public String getNickName() {
		Object props = attributes.get("response");
		if (props instanceof Map<?, ?> map) {
			Object nickname = map.get("nickname");
			return nickname != null ? nickname.toString() : null;
		}
		return null;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
