package com.postsmith.api.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDto {
	private String accessToken;
	private String userId;
	private String email;
	private String role;
	private String userNickname;
	private String profileImage;
}
