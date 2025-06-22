package com.postsmith.api.domain.feedContent.dto;

import java.time.LocalDateTime;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.CategoriesEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.entity.ContentsEntity.ContentEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentsDto {
	private Integer id;
	private Integer categoryId;
	private Integer blogId;
	private Integer sequence;
	private ContentEnum type;
	private String title;
	private String contentHtml;
	private String contentPlain;
	private String thumbnail; // 썸네일 이미지 URL
	private Boolean isTemp;
	private Boolean isPublic;
	private Integer likes;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private String blogAddress;
	private String categoryName;

	//에러가 난다면 blogAddresss랑 categoryName을 빼십쇼
	public static ContentsDto fromEntity(ContentsEntity entity) {
		return ContentsDto.builder()
				.id(entity.getId())
				.categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
				.categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
				.blogId(entity.getBlog().getId())
				.blogAddress(entity.getBlog() != null ? entity.getBlog().getAddress() : null)
				.sequence(entity.getSequence())
				.type(entity.getType())
				.title(entity.getTitle())
				.contentHtml(entity.getContentHtml())
				.contentPlain(entity.getContentPlain())
				.thumbnail(entity.getThumbnail())
				.isTemp(entity.getIsTemp())
				.isPublic(entity.getIsPublic())
				.likes(entity.getLikes())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}

	public ContentsEntity toEntity(CategoriesEntity category, BlogsEntity blog) {
		return ContentsEntity.builder().category(category).blog(blog).sequence(this.sequence).type(this.type).title(this.title).contentHtml(this.contentHtml)
				.contentPlain(this.contentPlain).thumbnail(this.thumbnail).isTemp(this.isTemp).isPublic(this.isPublic).likes(this.likes).build();
	}
}
