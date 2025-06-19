package com.postsmith.api.domain.content.dto;

import java.time.LocalDateTime;

import com.postsmith.api.entity.ContentsEntity.ContentEnum;

import lombok.Data;

@Data
public class PostsDto {
	private Integer blogId;
	private Integer contentId;
	private String userNickname;
	private ContentEnum contentType;
	private String title;
	private Integer sequence;
	private String address;

	private Integer categoryid;
	private String categoryName;
	private String categoryPath;

	private Boolean isPublic;
	private Integer likes;
	private LocalDateTime createdAt;
	private Integer totalViewCount;
	private Integer totalRepliesCount;
}
