package com.postsmith.api.domain.content.dto;

import java.util.List;

import lombok.Data;

@Data
public class PrivacyUpdateRequestDto {
	private List<Integer> contentIds;
	private Boolean isPublic;
}
