package com.postsmith.api.domain.replies.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RepliesCreateDto {
    private Integer userId;
    private Integer contentId;
    private Integer parentReplyId; // 대댓글인 경우 부모 댓글 ID
    private String contentText;
} 