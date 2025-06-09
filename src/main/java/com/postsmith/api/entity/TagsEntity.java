package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagsEntity {
	public enum TagEnum {
		CONTENT, THEME
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private TagEnum type; // 태그 유형

	@Column(name = "name", length = 255, nullable = false)
	private String name; // 태그 이름

	@Builder
	public TagsEntity(TagEnum type, String name) {
		this.type = type;
		this.name = name;
	}
}
