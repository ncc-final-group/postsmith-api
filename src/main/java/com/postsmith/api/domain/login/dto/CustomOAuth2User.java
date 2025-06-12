package com.postsmith.api.domain.login.dto;

import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.postsmith.api.entity.UsersEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class CustomOAuth2User implements OAuth2User {
	private final UsersEntity.RoleEnum role;

	@Override
	public String getName() {
		return "";
	}

	@Override
	public Map<String, Object> getAttributes() {
		return Map.of("provider", "");
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();

		collection.add(new GrantedAuthority() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return role.name();
			}
		});

		return collection;
	}

	public String getUsername() {
		return "";
	}
}
