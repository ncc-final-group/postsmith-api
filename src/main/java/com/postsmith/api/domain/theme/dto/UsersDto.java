package com.postsmith.api.domain.theme.dto;

import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.entity.UsersEntity.ProviderEnum;
import com.postsmith.api.entity.UsersEntity.RoleEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDto {
	private Integer id;
	private String uuid;
	private String email;
	private String password;
	private ProviderEnum provider;
	private RoleEnum role;
	private String nickname;
	private String profile_image;
	private String description;

	public static UsersDto fromEntity(UsersEntity entity) {
		return UsersDto.builder().id(entity.getId()).uuid(entity.getUuid()).email(entity.getEmail()).password(entity.getPassword()).provider(entity.getProvider())
				.role(entity.getRole()).nickname(entity.getNickname()).profile_image(entity.getProfileImage()).description(entity.getDescription()).build();
	}

	public UsersEntity toEntity() {
		return UsersEntity.builder().uuid(this.uuid).email(this.email).password(this.password).provider(this.provider).role(this.role).nickname(this.nickname)
				.profileImage(this.profile_image).description(this.description).build();
	}
}
