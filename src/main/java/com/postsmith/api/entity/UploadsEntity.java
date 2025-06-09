package com.postsmith.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "uploads")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private BlogsEntity blog; // FK > blogs.id

	@Column(name = "uri", length = 255, nullable = false)
	private String uri; // Storage URI

	@Column(name = "filename", length = 255)
	private String filename; // 표시할 파일 이름

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Builder
	public UploadsEntity(BlogsEntity blog, String uri, String filename) {
		this.blog = blog;
		this.uri = uri;
		this.filename = filename;
	}
}
