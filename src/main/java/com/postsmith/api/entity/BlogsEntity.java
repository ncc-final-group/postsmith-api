package com.postsmith.api.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blogs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
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

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Builder
	public BlogsEntity(UsersEntity user, String name, String nickname, String address, String description, String logoImage, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.user = user;
		this.name = name;
		this.nickname = nickname;
		this.description = description;
		this.logoImage = logoImage;
		this.address = address;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	public void updateBlogInfo(String name, String nickname, String address, String description, String logoImage) {
		if (name != null)
			this.name = name;
		if (nickname != null)
			this.nickname = nickname;
		if (address != null)
			this.address = address;
		if (description != null)
			this.description = description;
		if (logoImage != null)
			this.logoImage = logoImage;
	}

	public BlogsEntity(Integer id) {
		this.id = id;
	}
}
