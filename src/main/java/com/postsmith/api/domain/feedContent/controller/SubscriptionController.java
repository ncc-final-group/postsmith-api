package com.postsmith.api.domain.feedContent.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postsmith.api.domain.feedContent.dto.SubscriptionDto;
import com.postsmith.api.domain.feedContent.service.SubscriptionService;
import com.postsmith.api.domain.theme.dto.BlogsDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

	// 구독
	@PostMapping
	public ResponseEntity<String> subscribe(@RequestBody SubscriptionDto request) {
		subscriptionService.subscribe(request);
		return ResponseEntity.ok("Subscribed successfully.");
	}

	// 추천 구독 블로그
	@GetMapping("/recommendedBlogs/userId/{userId}")
	public List<BlogsDto> findrecommendedBlogs(@PathVariable("userId") Integer userId) {
		return subscriptionService.findrecommendedBlogs(userId);
	}

	// 구독
	@PutMapping("/subscription/subscriberId/{subscriberId}/blogId/{blogId}")
	public ResponseEntity<Void> subscribeBlog(@PathVariable("subscriberId") Integer subscriberId, @PathVariable("blogId") Integer blogId) {
		subscriptionService.subscribeBlog(subscriberId, blogId);
		return ResponseEntity.ok().build();
	}
}
