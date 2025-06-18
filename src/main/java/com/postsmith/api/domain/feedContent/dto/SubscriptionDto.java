package com.postsmith.api.domain.feedContent.dto;

import lombok.*;

@Getter
@Setter
public class SubscriptionDto {
	private Integer subscriberId; // 로그인한 사용자 ID
	private Integer blogId; // 구독할 대상 블로그 ID

	@Override
	public String toString() {
		return "SubscriptionDto{" + "subscriberId=" + subscriberId + ", blogId=" + blogId + '}';
	}
}
