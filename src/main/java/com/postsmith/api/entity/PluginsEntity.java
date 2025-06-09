package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plugins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PluginsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", length = 255, nullable = false)
	private String name; // 플러그인 이름

	@Column(name = "cover_image", length = 255, nullable = false)
	private String coverImage;

	@Column(name = "image", length = 255, nullable = false)
	private String image; // 플러그인 이미지 URI

	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 플러그인 설명

	@Column(name = "author", length = 255, nullable = false)
	private String author; // 플러그인 제작자

	@Column(name = "author_link", length = 255, nullable = false)
	private String authorLink; // 제작자 링크

	@Column(name = "plugins", columnDefinition = "TEXT")
	private String plugins; // 플러그인 내용

	@Column(name = "options", columnDefinition = "TEXT")
	private String options; // 플러그인 옵션(JSON)

	@Builder
	public PluginsEntity(String name, String coverImage, String image, String description, String author, String authorLink, String plugins, String options) {
		this.name = name;
		this.coverImage = coverImage;
		this.image = image;
		this.description = description;
		this.author = author;
		this.authorLink = authorLink;
		this.plugins = plugins;
		this.options = options;
	}
}
