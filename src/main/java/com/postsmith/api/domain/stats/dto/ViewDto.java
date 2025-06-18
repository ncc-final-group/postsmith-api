package com.postsmith.api.domain.stats.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ViewDto {
	private Integer blogId;
	private Integer views;
	private Integer visitors;
	private LocalDate date;

	// 생성자: JPQL new 구문에서 호출됨
	public ViewDto(Integer blogId, Long views, LocalDate date) {
		this.blogId = blogId;
		this.views = views != null ? views.intValue() : 0;
		this.visitors = 0; // 기본값 0으로 세팅
		this.date = date;
	}
}
