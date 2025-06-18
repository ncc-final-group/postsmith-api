package com.postsmith.api.domain.theme.dto;

import java.time.LocalDateTime;

import com.postsmith.api.entity.BlogsEntity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogsDto {
	private Integer id;
	private String name;
	private String nickname;
	private String address;
	private String description;
	private String logoImage;
	private Integer userId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static BlogsDto fromEntity(BlogsEntity entity) {
		return BlogsDto.builder().id(entity.getId()).name(entity.getName()).nickname(entity.getNickname()).address(entity.getAddress()).description(entity.getDescription())
				.logoImage(entity.getLogoImage()).userId(entity.getUser().getId()).createdAt(entity.getCreatedAt()).updatedAt(entity.getUpdatedAt()).build();
	}

	public BlogsEntity toEntity(com.postsmith.api.entity.UsersEntity user) {
		return BlogsEntity.builder().user(user).name(this.name).nickname(this.nickname).address(this.address).description(this.description).logoImage(this.logoImage).build();
	}

	@Override
	public String toString() {
		return "BlogsDto{" + "name='" + name + '\'' + ", nickname='" + nickname + '\'' + ", address='" + address + '\'' + ", logoImage='" + logoImage + '\'' + ", userId='" + userId
				+ '\'' + ", description='" + description + '\'' + '}';
	}
}
