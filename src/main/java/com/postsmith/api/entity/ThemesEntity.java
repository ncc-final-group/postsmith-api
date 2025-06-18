package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "themes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThemesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", length = 255, nullable = false)
	private String name; // 테마 이름

	@Column(name = "cover_image", length = 255, nullable = false)
	private String coverImage = ""; // 커버 이미지 URI

	@Column(name = "image", length = 255, nullable = false)
	private String image = ""; // 테마 이미지 URI

	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 테마 설명

	@Column(name = "author", length = 255, nullable = false)
	private String author; // 테마 제작자

	@Column(name = "author_link", length = 255, nullable = false)
	private String authorLink = ""; // 제작자 링크

	@Column(name = "html", columnDefinition = "TEXT")
	private String html; // 테마 내용

	@Column(name = "css", columnDefinition = "TEXT")
	private String css; // 테마 스타일

	@Builder
	public ThemesEntity(String name, String coverImage, String image, String description, String author, String authorLink, String html, String css) {
		this.name = name;
		this.coverImage = coverImage;
		this.image = image;
		this.description = description;
		this.author = author;
		this.authorLink = authorLink;
		this.html = html;
		this.css = css;
	}
}
