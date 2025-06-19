package com.postsmith.api.entity;

import java.time.LocalDateTime;

import com.postsmith.api.domain.manage.dto.ContentVisitsDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content_visits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentVisitsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", nullable = false, referencedColumnName = "id")
	private ContentsEntity content; // FK > contents.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@Setter
	private UsersEntity user; // FK > users.id

	@Column(name = "ip", length = 12, nullable = false, columnDefinition = "CHAR(12)")
	private String ip; // 접속 IP

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Builder
	public ContentVisitsEntity(ContentsEntity content, UsersEntity user, String ip) {
		this.content = content;
		this.user = user;
		this.ip = ip;
		this.createdAt = LocalDateTime.now();
	}
	public ContentVisitsDto toDto(){
		Integer userId;
		if(user == null){
			userId = null;
		} else {
			userId = this.user.getId();
		}
		return ContentVisitsDto.builder()
				.contentId(this.content.getId())
				.userId(userId)
				.ipAddress(this.ip)
				.createdAt(this.createdAt)
				.build();
	}
}
