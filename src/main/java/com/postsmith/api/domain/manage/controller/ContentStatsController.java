package com.postsmith.api.domain.manage.controller;

import com.postsmith.api.domain.manage.dto.ContentViewsDto;
import com.postsmith.api.domain.manage.dto.ContentVisitsDto;
import com.postsmith.api.domain.manage.service.ContentStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/content_stats")
public class ContentStatsController {
    private final ContentStatsService contentStatsService;
    @PostMapping("/view")
    public ContentViewsDto recordContentView(@RequestBody ContentViewsDto dto) {
        return contentStatsService.recordView(dto);
    }
    @PostMapping("/visit")
    public ContentVisitsDto recordContentVisit(@RequestBody ContentVisitsDto dto) {
        return contentStatsService.recordVisit(dto);
    }
    @GetMapping("/views/{contentId}")
    public Integer getTotalViewsByContentId(@PathVariable Integer contentId) {
        return contentStatsService.getTotalViewsByContentId(contentId);
    }
}
