package com.postsmith.api.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
	private String email;
	private String provider;
	private String role;
	private String nickname;
}
