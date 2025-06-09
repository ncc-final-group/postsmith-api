package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blog_themes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogThemesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private BlogsEntity blog; // FK > blogs.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theme_id", nullable = false, referencedColumnName = "id")
	private ThemesEntity theme; // FK > themes.id

	@Column(name = "theme_setting", columnDefinition = "TEXT")
	private String themeSetting; // 테마 설정(HTML)
	
	@Column(name = "is_active")
	private Boolean isActive; // 테마 활성화 여부

	@Builder
	public BlogThemesEntity(BlogsEntity blog, ThemesEntity theme, String themeSetting, Boolean isActive) {
		this.blog = blog;
		this.theme = theme;
		this.themeSetting = themeSetting;
		this.isActive = isActive;
	}
}
