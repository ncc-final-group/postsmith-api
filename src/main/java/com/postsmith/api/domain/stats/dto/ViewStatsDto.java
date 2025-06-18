package com.postsmith.api.domain.stats.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ViewStatsDto {
	private Integer blogId;
	private Integer todayViewCount;
	private Integer yesterdayViewCount;
	private Integer totalViewCount;
	private LocalDate today;

}
