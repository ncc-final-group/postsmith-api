package com.postsmith.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentsEntity {
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

	@Column(name = "is_temp")
	private Boolean isTemp; // 컨텐츠 임시 저장 여부

	@Column(name = "is_public")
	private Boolean isPublic; // 공개 여부

	@Column(name = "likes")
	private Integer likes; // 좋아요 수

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Builder
	public ContentsEntity(CategoriesEntity category, BlogsEntity blog, Integer sequence, ContentEnum type, String title, String contentHtml, String contentPlain, Boolean isTemp,
			Boolean isPublic, Integer likes) {
		this.category = category;
		this.blog = blog;
		this.sequence = sequence;
		this.type = type;
		this.title = title;
		this.contentHtml = contentHtml;
		this.contentPlain = contentPlain;
		this.isTemp = isTemp;
		this.isPublic = isPublic;
		this.likes = likes;
	}
}
