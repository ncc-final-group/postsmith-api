package com.postsmith.api.entity;

import java.time.LocalDate;

import com.postsmith.api.domain.manage.dto.ContentViewsDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content_views")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentViewsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", nullable = false, referencedColumnName = "id")
	private ContentsEntity content; // FK > contents.id

	@Column(name = "views_count")
	private Integer viewsCount; // 일일 조회수

	@Column(name = "created_on", insertable = false, updatable = false)
	private LocalDate createdOn; // not created_at

	@Builder
	public ContentViewsEntity(ContentsEntity content, Integer viewsCount, LocalDate createdOn) {
		this.content = content;
		this.viewsCount = viewsCount;
		this.createdOn = createdOn;
	}
	public void incrementViewsCount() {
		if (this.viewsCount == null) {
			this.viewsCount = 0;
		}
		this.viewsCount++;
	}

	public ContentViewsDto toDto() {
		return ContentViewsDto.builder()
				.id(this.id)
				.contentId(this.content.getId())
				.viewsCount(this.viewsCount)
				.createdOn(this.createdOn)
				.build();
	}
}
