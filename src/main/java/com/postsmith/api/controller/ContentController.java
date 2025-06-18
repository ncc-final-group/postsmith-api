package com.postsmith.api.controller;

import com.postsmith.api.dto.FeedContentsDto;
import com.postsmith.api.service.ContentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentsService;

    // 피드 컨텐츠(구독중인 블로글의 게시글) 찾기
    @GetMapping("/userId/{userId}")
    public List<FeedContentsDto> findFeedContents(@PathVariable("userId") Integer userId) {
        return contentsService.findFeedContents(userId);
    }

}
