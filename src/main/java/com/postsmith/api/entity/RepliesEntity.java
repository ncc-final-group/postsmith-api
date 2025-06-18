package com.postsmith.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "replies")
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RepliesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UsersEntity user; // FK > users.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", nullable = false, referencedColumnName = "id")
	private ContentsEntity content; // FK > contents.id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_id", referencedColumnName = "id")
	private RepliesEntity parentReply; // 부모 댓글 아이디: FK > replies.id

	@Column(name = "content", columnDefinition = "TEXT")
	private String contentText; // 댓글 내용

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "deleted_at", insertable = false, updatable = false)
	private LocalDateTime deletedAt;

    @Builder
    public RepliesEntity(UsersEntity user, ContentsEntity content, RepliesEntity parentReply, String contentText) {
        this.user = user;
        this.content = content;
        this.parentReply = parentReply;
        this.contentText = contentText;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // deletedAt 설정을 위한 메서드 추가
    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
