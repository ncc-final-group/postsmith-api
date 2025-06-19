package com.postsmith.api.domain.replies.controller;

import com.postsmith.api.domain.replies.dto.RepliesCreateDto;
import com.postsmith.api.domain.replies.dto.RepliesDto;
import com.postsmith.api.domain.replies.service.RepliesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replies")
@RequiredArgsConstructor
@Slf4j
public class RepliesController {

    private final RepliesService repliesService;

    /**
     * 댓글 생성 (POST)
     */
    @PostMapping
    public ResponseEntity<?> createReply(@RequestBody RepliesCreateDto createDto) {
        try {
            log.info("댓글 생성 요청: {}", createDto);
            RepliesDto createdReply = repliesService.createReply(createDto);
            log.info("댓글 생성 완료: {}", createdReply.getId());
            return ResponseEntity.ok(createdReply);
        } catch (IllegalArgumentException e) {
            log.error("댓글 생성 실패 - 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("댓글 생성 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("댓글 생성 중 오류가 발생했습니다.");
        }
    }

    /**
     * 콘텐츠별 댓글 조회 (GET)
     */
    @GetMapping("/content/{contentId}")
    public ResponseEntity<?> getRepliesByContentId(@PathVariable Integer contentId) {
        try {
            log.info("콘텐츠 {} 댓글 조회 요청", contentId);
            List<RepliesDto> replies = repliesService.getRepliesByContentId(contentId);
            log.info("콘텐츠 {} 댓글 {} 개 조회 완료", contentId, replies.size());
            return ResponseEntity.ok(replies);
        } catch (IllegalArgumentException e) {
            log.error("댓글 조회 실패 - 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("댓글 조회 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("댓글 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 댓글 삭제 (DELETE)
     */
    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(
            @PathVariable Integer replyId,
            @RequestParam Integer userId) {
        try {
            log.info("댓글 {} 삭제 요청 (사용자: {})", replyId, userId);
            repliesService.deleteReply(replyId, userId);
            log.info("댓글 {} 삭제 완료", replyId);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            log.error("댓글 삭제 실패 - 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("댓글 삭제 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }
}
