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
	@JoinColumn(name = "blog_id", referencedColumnName = "id", nullable = false)
	private BlogsEntity blog; // FK > blogs.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = true, referencedColumnName = "id")
	private CategoriesEntity parent; // FK > categories.id

	@Column(name = "name", length = 100, nullable = false)
	private String name; // 카테고리 이름

	@Column(name = "sequence")
	private Integer sequence; // 카테고리 배치 순서                                                                                                                       

	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 카테고리 설명

	@Builder
	public CategoriesEntity(BlogsEntity blog, CategoriesEntity parent, String name, Integer sequence, String description) {
		this.blog = blog;
		this.parent = parent;
		this.sequence = sequence;
		this.name = name;
		this.description = description;
	}

	public void updateCategory(BlogsEntity blog, CategoriesEntity parent, String name, Integer sequence, String description) {
		this.blog = blog;
		this.parent = parent;
		this.sequence = sequence;
		this.name = name;
		this.description = description;
	}
}
