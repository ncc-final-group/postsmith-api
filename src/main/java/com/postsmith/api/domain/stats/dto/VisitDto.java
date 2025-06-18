package com.postsmith.api.domain.stats.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class VisitDto {
	private Integer blogId;
	private Integer views;
	private Integer visitors;
	private LocalDate date;

	public VisitDto(Integer blogId, Long visitors, java.sql.Date date) {
		this.blogId = blogId;
		this.visitors = visitors != null ? visitors.intValue() : 0;
		this.date = date != null ? date.toLocalDate() : null;
		this.views = 0;
	}

}
