package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoriesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private BlogsEntity blog; // FK > blogs.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
	private CategoriesEntity category; // FK > categories.id

	@Column(name = "name", length = 100, nullable = false)
	private String name; // 카테고리 이름

	@Column(name = "sequence")
	private Integer sequence; // 카테고리 배치 순서

	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 카테고리 설명

	@Builder
	public CategoriesEntity(BlogsEntity blog, CategoriesEntity category, String name, String description) {
		this.blog = blog;
		this.category = category;
		this.name = name;
		this.description = description;
	}
}
