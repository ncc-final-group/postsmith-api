package com.postsmith.api.domain.feedContent.controller;

import com.postsmith.api.domain.feedContent.dto.FeedContentsDto;
import com.postsmith.api.domain.feedContent.service.FeedContentsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;


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
