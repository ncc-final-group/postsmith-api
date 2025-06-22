package com.postsmith.api.domain.manage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentsRequestDto {
	private Integer blogId;
	private Integer postId;
	private Integer category;
	private String title;
	private String content;
	private String thumbnail; // 썸네일 이미지 URL
	private String postType; // Default post type
	private Boolean isTemp = false;
	private Boolean isPublic = true;

	@Override
	public String toString() {
		return "PostDto{" + "blogId=" + blogId + ", postId=" + postId + ", category=" + category + ", title='" + title + '\'' + ", content='" + content + '\'' + ", thumbnail='" + thumbnail + '\'' + ", postType='"
				+ postType + '\'' + ", isTemp=" + isTemp + ", isPublic=" + isPublic + '}';
	}
}
