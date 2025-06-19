package com.postsmith.api.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "subscriber_id", nullable = false)
	private Integer subscriberId; // FK > users.id

	@Column(name = "blog_id", nullable = false)
	private Integer blogId; // FK > blog.id

	@Builder
	public SubscriptionId(Integer subscriberId, Integer blogId) {
		this.subscriberId = subscriberId;
		this.blogId = blogId;
	}
}
