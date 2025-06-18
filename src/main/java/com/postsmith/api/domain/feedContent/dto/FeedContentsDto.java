package com.postsmith.api.domain.feedContent.dto;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.domain.theme.dto.BlogsDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedContentsDto {
	private ContentsDto content;
	private BlogsDto blog;

	public static FeedContentsDto fromEntity(ContentsEntity contentEntity, BlogsEntity blogsEntity) {
		return FeedContentsDto.builder().content(ContentsDto.fromEntity(contentEntity)).blog(BlogsDto.fromEntity(blogsEntity)).build();
	}
}
