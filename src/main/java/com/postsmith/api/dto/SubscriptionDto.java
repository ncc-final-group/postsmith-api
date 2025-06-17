package com.postsmith.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequestDto {
    private Integer subscriberId; // 로그인한 사용자 ID
    private Integer blogId;    // 구독할 대상 블로그 ID
}
