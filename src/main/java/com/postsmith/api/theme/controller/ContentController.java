package com.postsmith.api.feedContent.controller;

<<<<<<<< HEAD:src/main/java/com/postsmith/api/theme/controller/ContentController.java
import com.postsmith.api.dto.FeedContentsDto;
import com.postsmith.api.service.ContentService;
========
import com.postsmith.api.feedContent.dto.FeedContentsDto;
import com.postsmith.api.feedContent.service.FeedContentsService;
>>>>>>>> develop:src/main/java/com/postsmith/api/feedContent/controller/FeedContentsController.java

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/feedContents")
@RequiredArgsConstructor
<<<<<<<< HEAD:src/main/java/com/postsmith/api/theme/controller/ContentController.java
public class ContentController {

    private final ContentService contentsService;
========
public class FeedContentsController {

    private final FeedContentsService contentsService;
>>>>>>>> develop:src/main/java/com/postsmith/api/feedContent/controller/FeedContentsController.java

    // 피드 컨텐츠(구독중인 블로글의 게시글) 찾기
    @GetMapping("/userId/{userId}")
    public List<FeedContentsDto> findFeedContents(@PathVariable("userId") Integer userId) {
        return contentsService.findFeedContents(userId);
    }

}
