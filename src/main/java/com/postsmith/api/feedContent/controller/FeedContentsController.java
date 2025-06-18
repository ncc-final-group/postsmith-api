package com.postsmith.api.feedContent.controller;

import com.postsmith.api.feedContent.dto.FeedContentsDto;
import com.postsmith.api.feedContent.service.FeedContentsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/feedContents")
@RequiredArgsConstructor
public class FeedContentsController {

    private final FeedContentsService contentsService;

    // 피드 컨텐츠(구독중인 블로글의 게시글) 찾기
    @GetMapping("/userId/{userId}")
    public List<FeedContentsDto> findFeedContents(@PathVariable("userId") Integer userId) {
        return contentsService.findFeedContents(userId);
    }

}
