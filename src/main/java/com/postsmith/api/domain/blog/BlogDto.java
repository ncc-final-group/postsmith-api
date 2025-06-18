package com.postsmith.api.domain.blog;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.UsersEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogDto {
	private Integer id;
	private Integer userId;
	private String name;
	private String nickname;
	private String address;
	private String description;
	private String logoImage;

	public BlogsEntity toEntity(UsersEntity user) {
		return BlogsEntity.builder().user(user).name(this.name).nickname(this.nickname).address(this.address).description(this.description)
				.logoImage(this.logoImage != null ? this.logoImage : "default-logo.png").build();
	}

	@Override
	public String toString() {
		return "BlogDto{" + "id=" + id + ", userId=" + userId + ", name='" + name + '\'' + ", nickname='" + nickname + '\'' + ", address='" + address + '\'' + ", description='"
				+ description + '\'' + ", logoImage='" + logoImage + '\'' + '}';
	}
}