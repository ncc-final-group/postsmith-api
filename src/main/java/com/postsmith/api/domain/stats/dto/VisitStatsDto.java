package com.postsmith.api.domain.stats.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VisitStatsDto {
	private Integer blogId;
	private Integer todayVisitCount;
	private Integer yesterdayVisitCount;
	private Integer totalVisitCount;
	private LocalDate today;

}
