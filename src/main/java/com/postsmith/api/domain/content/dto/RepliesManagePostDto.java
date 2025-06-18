package com.postsmith.api.domain.content.dto;

import lombok.Data;

@Data
public class RepliesManagePostDto {
	private Integer userId;
	private Integer contentId;
	private Integer replyId;
	private String contentText;

}
