package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CategoriesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id", referencedColumnName = "id", nullable = false)
	private BlogsEntity blog; // FK > blogs.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = true)
	private CategoriesEntity category; // 상위 카테고리

	@Column(name = "name", length = 100, nullable = false)
	private String name; // 카테고리 이름

	@Column(name = "sequence")
	private Integer sequence; // 카테고리 배치 순서

	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 카테고리 설명

	@Builder
	public CategoriesEntity(BlogsEntity blog, CategoriesEntity category, Integer sequence, String name, String description) {
		if (blog == null) {
			throw new IllegalArgumentException("Blog must not be null");
		}
		this.blog = blog;
		this.category = category;
		this.sequence = sequence;
		this.name = name;
		this.description = description;
	}

	public void setCategory(CategoriesEntity category) {
		this.category = category;
	}
}
