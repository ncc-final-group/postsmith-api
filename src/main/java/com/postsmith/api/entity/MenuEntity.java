package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity {
	public enum MenuEnum {
		DEFAULT, PAGE, CATEGORY, MANUAL
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private BlogsEntity blog; // FK > blogs.id

	@Column(name = "name", length = 255, nullable = false)
	private String name; // 메뉴 이름

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private MenuEnum type; // 메뉴 유형

	@Column(name = "uri", length = 255, nullable = false)
	private String uri; // 연결 uri

	@Column(name = "is_blank", nullable = false)
	private Boolean isBlank; // 새 창에서 열기 여부

	@Builder
	public MenuEntity(BlogsEntity blog, String name, MenuEnum type, String uri, Boolean isBlank) {
		this.blog = blog;
		this.name = name;
		this.type = type;
		this.uri = uri;
		this.isBlank = isBlank;
	}
}
