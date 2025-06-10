package com.example.postsmith_api.controller.media;

import com.example.postsmith_api.dto.media.MediaFileDto;
import com.example.postsmith_api.service.media.MediaFileService;
import lombok.RequiredArgsConstructor;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MediaManageController {

    private final MediaFileService mediaFileService;

    // 미디어 파일 목록 조회
    @GetMapping
    public ResponseEntity<Page<MediaFileDto>> getMediaFiles(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MediaFileDto> mediaFiles;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 키워드 검색
            mediaFiles = mediaFileService.searchMediaFiles(userId, keyword.trim(), pageable);
        } else if (type != null && !type.trim().isEmpty()) {
            // 타입별 조회
            mediaFiles = mediaFileService.getUserMediaFilesByType(userId, type, pageable);
        } else {
            // 전체 조회
            mediaFiles = mediaFileService.getUserMediaFiles(userId, pageable);
        }

        return ResponseEntity.ok(mediaFiles);
    }

    // 미디어 파일 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<MediaFileDto> getMediaFile(@PathVariable Long id) {
        MediaFileDto mediaFile = mediaFileService.getMediaFile(id);
        return ResponseEntity.ok(mediaFile);
    }

    // 미디어 파일 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<MediaFileDto> updateMediaFile(
            @PathVariable Long id,
            @RequestBody MediaFileDto updateDto) {
        MediaFileDto updatedFile = mediaFileService.updateMediaFile(id, updateDto);
        return ResponseEntity.ok(updatedFile);
    }

    // 미디어 파일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaFile(@PathVariable Long id) {
        mediaFileService.deleteMediaFile(id);
        return ResponseEntity.ok().build();
    }

    // 여러 미디어 파일 삭제
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteMediaFiles(@RequestBody List<Long> ids) {
        mediaFileService.deleteMediaFiles(ids);
        return ResponseEntity.ok().build();
    }

    // 사용자 미디어 통계
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserMediaStats(@RequestParam Integer userId) {
        Map<String, Object> stats = mediaFileService.getUserMediaStats(userId);
        return ResponseEntity.ok(stats);
    }
} 