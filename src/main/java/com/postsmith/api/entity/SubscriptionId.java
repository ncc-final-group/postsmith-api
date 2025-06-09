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

	@Column(name = "blogger_id", nullable = false)
	private Integer bloggerId; // FK > users.id

	@Builder
	public SubscriptionId(Integer subscriberId, Integer bloggerId) {
		this.subscriberId = subscriberId;
		this.bloggerId = bloggerId;
	}
}
