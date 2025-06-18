package com.postsmith.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionEntity {

	@EmbeddedId
	private SubscriptionId id;

	@MapsId("subscriberId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subscriber_id", nullable = false, referencedColumnName = "id")
	private UsersEntity subscriber; // 구독자 아이디: FK > users.id

	@MapsId("blogId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private BlogsEntity blog; // 구독 대상 블로그 아이디: FK > blogs.id

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Builder
	public SubscriptionEntity(UsersEntity subscriber, BlogsEntity blog) {
		this.subscriber = subscriber;
		this.blog = blog;
		this.id = SubscriptionId.builder().subscriberId(subscriber.getId()).blogId(blog.getId()).build();
		this.createdAt = LocalDateTime.now();
	}
}
