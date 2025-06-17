package com.postsmith.api.content.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({
    "repliesId",
    "userName",
    "parentReplyId",
    "replyContent",
    "contentTitle",
    "createdAt"
})
public class RepliesManageDto {
    private Integer RepliesId;
    private String UserName;
    private Integer ParentReplyId;
    private String ReplyContent;
    private String ContentTitle;
    private LocalDateTime CreatedAt;
}
