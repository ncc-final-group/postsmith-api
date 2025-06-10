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
	private String filename; // 표시할 파일 이름

	@Column(name = "type", length = 50)
	private String fileType; // 파일 타입 (예: image/jpeg, image/png 등)

	@Column(name = "size")
	private Integer fileSize; // 파일 크기 (바이트 단위)

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Builder
	public MediaEntity(BlogsEntity blog, String uri, String filename, String fileType, Integer fileSize) {
		this.blog = blog;
		this.uri = uri;
		this.filename = filename;
		this.fileType = fileType;
		this.fileSize = fileSize;
	}
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
	
	public void updateMediaInfo(String filename, String fileType) {
		if (filename != null) this.filename = filename;
		if (fileType != null) this.fileType = fileType;
	}
}
