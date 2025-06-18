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

    /** 구독자 (users.id) */
/*
    @MapsId("subscriberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false, referencedColumnName = "id")
    private UsersEntity subscriber;

    /** 구독 대상 블로그 (blogs.id)
    @MapsId("blogId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
    private BlogsEntity blog;
	@MapsId("blogId")
	@ManyToOne
	@JoinColumn(name = "blog_id", nullable = false, referencedColumnName = "id")
	private UsersEntity blogid; // 구독 대상 아이디: FK > users.id

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 편의 생성자: builder 사용 시 createdAt 자동 세팅
    @Builder
    public SubscriptionEntity(UsersEntity subscriber, BlogsEntity blog) {
        this.subscriber = subscriber;
        this.blog       = blog;
        this.id         = SubscriptionId.builder()
                              .subscriberId(subscriber.getId())
                              .blogId(blog.getId())
                              .build();
        this.createdAt  = LocalDateTime.now();
    }
	@Builder
	public SubscriptionEntity(UsersEntity subscriber, UsersEntity blogid) {
		this.id = SubscriptionId.builder().subscriberId(subscriber.getId()).bloggerId(blogid.getId()).build();
		this.subscriber = subscriber;
		this.blogid = blogid;
	}
}
*/
