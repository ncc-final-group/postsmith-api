package com.postsmith.api.domain.content.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "repliesId", "userName", "parentReplyId", "replyContent", "contentTitle", "contentId", "createdAt" })
public class RepliesManageDto {
	private Integer RepliesId;
	private String UserName;
	private Integer ParentReplyId;
	private String ReplyContent;
	private String ContentTitle;
	private Integer ContentId;
	private LocalDateTime CreatedAt;
	private String address;
	private Integer sequence;
}
