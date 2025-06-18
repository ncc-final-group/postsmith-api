package com.postsmith.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationsEntity {
	public enum NotificationEnum {
		POSTS, SUBSCRIPTION, COMMENT
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
	private UsersEntity user; // FK > users.id

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationEnum type; // 알림 유형

	@Column(name = "is_read", nullable = false)
	private Boolean isRead; // 읽음 여부

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Builder
	public NotificationsEntity(UsersEntity user, NotificationEnum type, Boolean isRead) {
		this.user = user;
		this.type = type;
		this.isRead = isRead;
	}
}
