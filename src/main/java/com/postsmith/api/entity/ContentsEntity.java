package com.postsmith.api.entity;

import java.time.LocalDateTime;

import com.postsmith.api.domain.manage.dto.ContentsResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentsEntity {
	public ContentsResponseDto toDto() {
		return ContentsResponseDto.builder().id(this.id).categoryId(this.category != null ? this.category.getId() : null).blogId(this.blog.getId()).sequence(this.sequence)
				.postType(this.type.name()).title(this.title).contentHtml(this.contentHtml).contentPlain(this.contentPlain).thumbnail(this.thumbnail).isTemp(this.isTemp).isPublic(this.isPublic)
				.likes(this.likes != null ? this.likes : 0).createdAt(this.createdAt).updatedAt(this.updatedAt).build();
	}

	public enum ContentEnum {
		POSTS, PAGE, NOTICE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private CategoriesEntity category; // FK > categories.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private BlogsEntity blog; // FK > blogs.id

	@Column(name = "sequence", nullable = false)
	private Integer sequence; // 컨텐츠 번호

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private ContentEnum type; // 컨텐츠 글 유형

	@Column(name = "title", length = 255, nullable = false)
	private String title; // 컨텐츠 제목

	@Column(name = "content_html", columnDefinition = "TEXT")
	private String contentHtml; // 컨텐츠 HTML

	@Column(name = "content_plain", columnDefinition = "TEXT")
	private String contentPlain; // 태그를 뺀 텍스트

	@Column(name = "thumbnail", columnDefinition = "TEXT")
	private String thumbnail; // 컨텐츠 썸네일 이미지 URL

	@Column(name = "is_temp")
	private Boolean isTemp; // 컨텐츠 임시 저장 여부

	@Column(name = "is_public")
	private Boolean isPublic; // 공개 여부

	@Column(name = "likes")
	private Integer likes; // 좋아요 수

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Builder
	public ContentsEntity(CategoriesEntity category, BlogsEntity blog, Integer sequence, ContentEnum type, String title, String contentHtml, String contentPlain, String thumbnail, Boolean isTemp,
			Boolean isPublic, Integer likes) {
		this.category = category;
		this.blog = blog;
		this.sequence = sequence;
		this.type = type;
		this.title = title;
		this.contentHtml = contentHtml;
		this.contentPlain = contentPlain;
		this.thumbnail = thumbnail;
		this.isTemp = isTemp;
		this.isPublic = isPublic;
		this.likes = likes;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	// 업데이트를 위한 메서드들
	public void updateContent(String title, String contentHtml, String contentPlain, String thumbnail, Boolean isTemp, Boolean isPublic) {
		this.title = title;
		this.contentHtml = contentHtml;
		this.contentPlain = contentPlain;
		this.thumbnail = thumbnail;
		this.isTemp = isTemp;
		this.isPublic = isPublic;
	}

	public void updateCategory(CategoriesEntity category) {
		this.category = category;
	}

	public void updateType(ContentEnum type) {
		this.type = type;
	}

	public void incrementLikes() {
		this.likes = (this.likes == null ? 0 : this.likes) + 1;
	}

	public void decrementLikes() {
		this.likes = (this.likes == null || this.likes <= 0) ? 0 : this.likes - 1;
	}
}
