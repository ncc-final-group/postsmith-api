package com.postsmith.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postsmith.api.dto.SubscriptionRequestDto;
import com.postsmith.api.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // 구독
    @PostMapping
    public ResponseEntity<String> subscribe(@RequestBody SubscriptionRequestDto request) {
        subscriptionService.subscribe(request);
        return ResponseEntity.ok("Subscribed successfully.");
    }

}
