package com.postsmith.api.domain.replies.dto;

import com.postsmith.api.entity.RepliesEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RepliesDto {
    private Integer id;
    private Integer userId;
    private Integer contentId;
    private Integer parentReplyId; // 부모 댓글 ID (대댓글인 경우)
    private String contentText;
    private String userNickname; // 사용자 닉네임 (조회용)
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    // Entity를 DTO로 변환
    public static RepliesDto fromEntity(RepliesEntity entity) {
        return RepliesDto.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .contentId(entity.getContent() != null ? entity.getContent().getId() : null)
                .parentReplyId(entity.getParentReply() != null ? entity.getParentReply().getId() : null)
                .contentText(entity.getContentText())
                .userNickname(entity.getUser() != null ? entity.getUser().getNickname() : null)
                .createdAt(entity.getCreatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
} 