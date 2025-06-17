/*
package com.postsmith.api.domain.manage.controller;

import com.postsmith.api.domain.manage.dto.MediaDto;
import com.postsmith.api.domain.manage.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api1/media")
@RequiredArgsConstructor
@Slf4j
public class MediaManageController {

    private final MediaService mediaService;

    @GetMapping
    public ResponseEntity<Page<MediaDto>> getMediaFiles(
            @RequestParam(name = "userId") Integer userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String search) {

        log.info("미디어 파일 목록 조회 요청: userId={} (무시됨), page={}, size={}, fileType={}, search={}",
                userId, page, size, fileType, search);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MediaDto> mediaFiles;

            if (search != null && !search.trim().isEmpty()) {
                mediaFiles = mediaService.searchMediaFiles(userId, search.trim(), pageable);
            } else if (fileType != null && !fileType.trim().isEmpty()) {
                mediaFiles = mediaService.getBlogMediaFilesByType(userId, fileType.trim(), pageable);
            } else {
                mediaFiles = mediaService.getBlogMediaFiles(userId, pageable);
            }

            log.info("미디어 파일 목록 조회 완료: 총 {}개 파일", mediaFiles.getTotalElements());
            return ResponseEntity.ok(mediaFiles);
        } catch (Exception e) {
            log.error("미디어 파일 목록 조회 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaDto> getMediaFile(
            @PathVariable Integer id,
            @RequestParam(name = "userId") Integer userId) {

        try {
            MediaDto mediaFile = mediaService.getMediaFile(id);
            log.info("미디어 파일 조회 완료: {}", mediaFile.getFilename());
            return ResponseEntity.ok(mediaFile);
        } catch (Exception e) {
            log.error("미디어 파일 조회 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaDto> updateMediaFile(
            @PathVariable Integer id,
            @RequestBody MediaDto updateDto,
            @RequestParam(name = "userId") Integer userId) {

        try {
            MediaDto updatedMedia = mediaService.updateMediaFile(id, updateDto);
            log.info("미디어 파일 수정 완료: {}", updatedMedia.getFilename());
            return ResponseEntity.ok(updatedMedia);
        } catch (Exception e) {
            log.error("미디어 파일 수정 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMediaFile(
            @PathVariable Integer id,
            @RequestParam(name = "userId") Integer userId) {

        log.info("미디어 파일 삭제 요청: id={}, userId={} (무시됨)", id, userId);

        try {
            mediaService.deleteMediaFile(id);
            log.info("미디어 파일 삭제 완료: id={}", id);
            return ResponseEntity.ok("미디어 파일이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("미디어 파일 삭제 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/batch")
    public ResponseEntity<String> deleteMediaFiles(
            @RequestBody List<Integer> ids,
            @RequestParam(name = "userId") Integer userId) {

        log.info("미디어 파일 일괄 삭제 요청: ids={}, userId={} (무시됨)", ids, userId);

        try {
            mediaService.deleteMediaFiles(ids);
            log.info("미디어 파일 일괄 삭제 완료: {} 개 파일", ids.size());
            return ResponseEntity.ok(ids.size() + "개의 미디어 파일이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("미디어 파일 일괄 삭제 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMediaStats(
            @RequestParam(name = "userId") Integer userId) {

        log.info("=== 미디어 통계 조회 요청 시작 ===");
        try {
            log.info("MediaService.getBlogMediaStats 호출 중...");
            Map<String, Object> stats = mediaService.getBlogMediaStats(userId);
            log.info("MediaService.getBlogMediaStats 호출 완료: {}", stats);
            log.info("=== 미디어 통계 조회 성공 ===");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("=== 미디어 통계 조회 실패 ===");
            log.error("에러 메시지: {}", e.getMessage());
            log.error("에러 스택 트레이스:", e);
            throw e;
        }
    }
} */
