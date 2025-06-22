package com.postsmith.api.domain.replies.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RepliesUpdateDto {
    private Integer userId;
    private String contentText;
} 