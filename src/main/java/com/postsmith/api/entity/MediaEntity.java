package com.postsmith.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MediaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private BlogsEntity blog; // FK > blogs.id

	@Column(name = "uri", length = 255, nullable = false)
	private String uri; // Storage URI

	@Column(name = "name", length = 255)
	private String name; // 표시할 파일 이름

	@Column(name = "type", length = 255)
	private String type; // 미디어 파일 형식

	@Column(name = "size")
	private int size; // 미디어 파일 사이즈(bytes)

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Builder
	public MediaEntity(BlogsEntity blog, String uri, String name, String type, int size) {
		this.blog = blog;
		this.uri = uri;
		this.name = name;
		this.type = type;
		this.size = size;
	}
	public void updateMediaInfo(String filename, String fileType) {
		if (filename != null) this.name = filename;
		if (fileType != null) this.type = fileType;
	}
}
