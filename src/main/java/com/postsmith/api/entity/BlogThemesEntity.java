package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blog_themes", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"blog_id", "theme_id"})
})
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

	@Column(name = "theme_html", columnDefinition = "TEXT")
	private String themeHtml; // 테마 HTML 코드
	@Column(name = "theme_css", columnDefinition = "TEXT")
	private String themeCss; // 테마 CSS 코드
	@Column(name = "is_active")
	private Boolean isActive; // 테마 활성화 여부

	@Builder
	public BlogThemesEntity(BlogsEntity blog, ThemesEntity theme, String themeHtml, String themeCss, String themeSetting, Boolean isActive) {
		this.blog = blog;
		this.theme = theme;
		this.themeHtml = themeHtml;
		this.themeCss = themeCss;
		this.themeSetting = themeSetting;
		this.isActive = isActive;
	}
	
	// 테마 콘텐츠 업데이트
	public void updateThemeContent(String themeHtml, String themeCss) {
		if (themeHtml != null) this.themeHtml = themeHtml;
		if (themeCss != null) this.themeCss = themeCss;
	}
	
	// 테마 설정 업데이트
	public void updateThemeSetting(String themeSetting) {
		if (themeSetting != null) this.themeSetting = themeSetting;
	}
	
	// 활성화 상태 변경
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	// 활성화 상태 업데이트
	public void updateIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	// 테마 HTML 업데이트
	public void updateThemeHtml(String themeHtml) {
		if (themeHtml != null) this.themeHtml = themeHtml;
	}
	
	// 테마 CSS 업데이트
	public void updateThemeCss(String themeCss) {
		if (themeCss != null) this.themeCss = themeCss;
	}
	
	public void updateTheme(ThemesEntity theme){
		if (theme != null) {
			this.theme = theme;
			this.themeHtml = theme.getHtml(); // 원본 HTML
			this.themeCss = theme.getCss();   // 원본 CSS
		}
	}
}
