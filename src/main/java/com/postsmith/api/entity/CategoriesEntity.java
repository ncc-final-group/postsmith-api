package com.postsmith.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CategoriesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private BlogsEntity blog; // FK > blogs.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private CategoriesEntity parent; // FK > categories.id (자기 참조, nullable)

	@Column(name = "name", length = 100, nullable = false)
	private String name; // 카테고리 이름

	@Column(name = "sequence")
	private Integer sequence; // 카테고리 배치 순서

	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 카테고리 설명

	@Builder
	public CategoriesEntity(BlogsEntity blog, CategoriesEntity parent, String name, String description) {
		this.blog = blog;
		this.parent = parent;
		this.name = name;
		this.description = description;
	}
}
