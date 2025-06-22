package com.postsmith.api.domain.manage.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContentsResponseDto {
	private Integer id;
	private Integer blogId;
	private Integer postId;
	private Integer categoryId;
	private Integer sequence;
	private String title;
	private String contentHtml;
	private String contentPlain;
	private String thumbnail; // 썸네일 이미지 URL
	private String postType; // "POSTS" or "PAGES"
	private Boolean isTemp; // Temporary post
	private Boolean isPublic; // Public visibility
	private Integer likes; // Number of likes
	private LocalDateTime createdAt; // Creation timestamp
	private LocalDateTime updatedAt; // Last update timestamp

}
