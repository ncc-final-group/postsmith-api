package com.postsmith.api.domain.login.dto;

import java.util.Map;

public class KakaoOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attributes;

	public KakaoOAuth2Response(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		Object id = attributes.get("id");
		return id != null ? id.toString() : null;
	}

	@Override
	public String getEmail() {
		Object props = attributes.get("kakao_account");
		if (props instanceof Map<?, ?> map) {
			Object email = map.get("email");
			return email != null ? email.toString() : null;
		}
		return null;
	}

	@Override
	public String getNickName() {
		Object props = attributes.get("properties");
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
