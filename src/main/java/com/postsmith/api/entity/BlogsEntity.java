package com.postsmith.api.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.postsmith.api.domain.blog.BlogDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blogs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BlogsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
	private UsersEntity user; // FK > users.id

	@Column(name = "name", length = 255, nullable = false)
	private String name; // 블로그 이름

	@Column(name = "nickname", length = 255, nullable = false)
	private String nickname; // 블로그 닉네임
	
	@Column(name = "address", length = 32, nullable = false)
	private String address; // 블로그 주소

	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 블로그 설명

	@Column(name = "logo_image", length = 255, nullable = false)
	private String logoImage; // 로고 이미지 URI

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theme_id", referencedColumnName = "id")
	private ThemesEntity theme; // FK > themes.id

	@Column(name = "theme_html", columnDefinition = "TEXT")
	private String themeHtml; // 블로그 테마 HTML

	@Column(name = "theme_css", columnDefinition = "TEXT")
	private String themeCss; // 블로그 테마 CSS

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Builder
	public BlogsEntity(UsersEntity user, String name, String nickname, String address, String description, String logoImage, ThemesEntity theme, String themeHtml, String themeCss) {
		this.user = user;
		this.name = name;
		this.nickname = nickname;
		this.address = address;
		this.description = description;
		this.logoImage = logoImage;
		this.theme = theme;
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
	
	public void updateBlogInfo(String name, String nickname, String address, String description, String logoImage, ThemesEntity theme, String themeHtml, String themeCss) {
		if (name != null) this.name = name;
		if (nickname != null) this.nickname = nickname;
		if (address != null) this.address = address;
		if (description != null) this.description = description;
		if (logoImage != null) this.logoImage = logoImage;
		if (theme != null) this.theme = theme;
		if (themeHtml != null) this.themeHtml = themeHtml;
		if (themeCss != null) this.themeCss = themeCss;
	}
	public BlogDto toDto() {
		return BlogDto.builder()
				.id(this.id)
				.userId(this.user.getId())
				.name(this.name)
				.nickname(this.nickname)
				.address(this.address)
				.description(this.description)
				.logoImage(this.logoImage)
				.themeId(this.theme != null ? this.theme.getId() : null)
				.themeHtml(this.themeHtml)
				.themeCss(this.themeCss)
				.build();
	}
}
