package com.postsmith.api.domain.stats.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.postsmith.api.domain.stats.dto.ViewStatsDto;
import com.postsmith.api.domain.stats.dto.VisitDto;
import com.postsmith.api.domain.stats.dto.ViewDto;
import com.postsmith.api.domain.stats.dto.VisitStatsDto;
import com.postsmith.api.domain.stats.service.StatsService;

@RestController
@RequestMapping("/api/Stats")
public class StatsController {

	private final StatsService statsService;

	public StatsController(StatsService statsService) {
		this.statsService = statsService;
	}

	@GetMapping("view/{blogId}")
	public ResponseEntity<ViewStatsDto> getViewStats(@PathVariable("blogId") Integer blogId) {
		ViewStatsDto viewStats = statsService.getViewStatsByBlogId(blogId);
		return ResponseEntity.ok(viewStats);
	}

	@GetMapping("visit/{blogId}")
	public ResponseEntity<VisitStatsDto> getVisitStats(@PathVariable("blogId") Integer blogId) {
		VisitStatsDto visitStats = statsService.getVisitStatsByBlogId(blogId);
		return ResponseEntity.ok(visitStats);
	}

	@GetMapping("each/view/{contentId}")
	public ResponseEntity<ViewStatsDto> getEachViewStats(@PathVariable("contentId") Integer contentId) {
		ViewStatsDto viewStats = statsService.getViewStatsByContentId(contentId);
		return ResponseEntity.ok(viewStats);
	}

	@GetMapping("each/visit/{contentId}")
	public ResponseEntity<VisitStatsDto> getEachVisitStats(@PathVariable("contentId") Integer contentId) {
		VisitStatsDto visitStats = statsService.getVisitStatsByContentId(contentId);
		return ResponseEntity.ok(visitStats);
	}

	@GetMapping("/views/daily")
	public List<ViewDto> getDailyViews(@RequestParam("blogId") Integer blogId) {
		return statsService.getDailyViewsByBlogId(blogId);
	}

	// 방문자 통계
	@GetMapping("/visit/daily")
	public List<VisitDto> getDailyVisitors(@RequestParam("blogId") Integer blogId) {
		return statsService.getDailyVisitorsByBlogId(blogId);
	}

	@GetMapping("each/views/daily")
	public List<ViewDto> getDailyEachViews(@RequestParam("contentId") Integer contentId) {
		return statsService.getDailyEachViewsByBlogId(contentId);
	}

	// 방문자 통계
	@GetMapping("each/visit/daily")
	public List<VisitDto> getDailyEachVisitors(@RequestParam("contentId") Integer contentId) {
		return statsService.getDailyEachVisitorsByBlogId(contentId);
	}

}
