/*
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
	@ManyToOne
	@JoinColumn(name = "subscriber_id", nullable = false, referencedColumnName = "id")
	private UsersEntity subscriber; // 구독자 아이디: FK > users.id

	@MapsId("blogId")
	@ManyToOne
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private UsersEntity blogid; // 구독 대상 아이디: FK > users.id

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Builder
	public SubscriptionEntity(UsersEntity subscriber, UsersEntity blogid) {
		this.id = SubscriptionId.builder().subscriberId(subscriber.getId()).bloggerId(blogid.getId()).build();
		this.subscriber = subscriber;
		this.blogid = blogid;
	}
}
*/
