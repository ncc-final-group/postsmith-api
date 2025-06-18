package com.postsmith.api.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UsersEntity {
	public enum ProviderEnum {
		EMAIL, GOOGLE, GITHUB, NAVER, KAKAO
	}

	public enum RoleEnum {
		ADMIN, USER
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "uuid", length = 36, unique = true, nullable = false, columnDefinition = "CHAR(36)")
	private String uuid; // 유저 UUIDv4

	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email; // 이메일

	@Column(name = "password", length = 60, nullable = false, columnDefinition = "CHAR(60)")
	private String password; // 비밀번호(Bcrypt: 60Bytes)

	@Enumerated(EnumType.STRING)
	@Column(name = "provider", length = 10, nullable = false)
	private ProviderEnum provider; // OAuth 유형(+Email)

	@Enumerated(EnumType.STRING)
	@Column(name = "role", length = 10, nullable = false)
	private RoleEnum role; // 역할(권한 관리)

	@Column(name = "nickname", length = 255, nullable = false)
	private String nickname; // 닉네임

	@Column(name = "profile_image", length = 255)
	private String profileImage; // 프로필이미지 URI

	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 자기소개

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Builder
	public UsersEntity(String uuid, String email, String password, ProviderEnum provider, RoleEnum role, String nickname, String profileImage, String description) {
		this.uuid = uuid;
		this.email = email;
		this.password = password;
		this.provider = provider;
		this.role = role;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.description = description;
	}
}
