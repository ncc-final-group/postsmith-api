package com.postsmith.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

	// 시퀀스 변경을 위한 비즈니스 메서드
	public void changeSequence(Integer sequence) {
		this.sequence = sequence;
	}

	// 카테고리 이동을 위한 비즈니스 메서드
	public void changeCategory(CategoriesEntity parent) {
		this.parent = parent;
	}

	@Builder
	public CategoriesEntity(BlogsEntity blog, CategoriesEntity parent, Integer sequence, String name, String description) {
		if (blog == null) {
			throw new IllegalArgumentException("Blog must not be null");
		}
		this.blog = blog;
		this.parent = parent;
		this.sequence = sequence;
		this.name = name;
		this.description = description;
	}

	public void changeName(String name) {this.name = name;}
	public void changeDescription(String description) {this.description = description;}


}
