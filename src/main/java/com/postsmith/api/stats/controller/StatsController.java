package com.postsmith.api.stats.controller;




import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.postsmith.api.stats.dto.ViewStatsDto;
import com.postsmith.api.stats.dto.VisitDto;
import com.postsmith.api.stats.dto.ViewDto;
import com.postsmith.api.stats.dto.VisitStatsDto;
import com.postsmith.api.stats.service.StatsService;

@RestController
@RequestMapping("/api/Stats")
public class StatsController {

  private final StatsService statsService;
  
  public StatsController(StatsService statsService) {
	  this.statsService =statsService;
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
  
  @GetMapping("/views/daily")
  public List<ViewDto> getDailyViews(@RequestParam("blogId") Integer blogId) {
      return statsService.getDailyViewsByBlogId(blogId);
  }

  // 방문자 통계
  @GetMapping("/visit/daily")
  public List<VisitDto> getDailyVisitors(@RequestParam("blogId") Integer blogId) {
      return statsService.getDailyVisitorsByBlogId(blogId);
  }

}
