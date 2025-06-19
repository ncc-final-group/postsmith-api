package com.postsmith.api.domain.content.controller;

import com.postsmith.api.domain.content.dto.MainContentsDto;
import com.postsmith.api.domain.content.service.MainContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/mainContents")
@RequiredArgsConstructor
public class MainContentsController {

    private final MainContentsService mainContentsService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<MainContentsDto>>> getRecommendedAndRecent() {
        Map<String, List<MainContentsDto>> result = new HashMap<>();
        result.put("recommended", mainContentsService.getTop3RecommendedContents());
        result.put("latest", mainContentsService.getRandomRecentContents());


        return ResponseEntity.ok(result);
    }

    @GetMapping("/blog-address/{userId}")
    public ResponseEntity<String> getBlogAddress(@PathVariable Integer userId) {
        String address = mainContentsService.getBlogAddressByUserId(userId);
        return ResponseEntity.ok(address);
    }
}
