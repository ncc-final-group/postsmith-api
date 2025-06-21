package com.postsmith.api.domain.manage.controller;

import com.postsmith.api.domain.manage.dto.MediaDto;
import com.postsmith.api.domain.manage.service.FileUploadService;
import com.postsmith.api.domain.manage.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final MediaService mediaFileService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "altText", required = false) String altText,
                                       @RequestParam(value = "userId", required = false) Integer userId,
                                       @RequestParam(value = "blogId", required = false) Integer blogId) {
        log.info("=== 이미지 업로드 시작 ===");
        log.info("파일명: {}, 크기: {} bytes", file.getOriginalFilename(), file.getSize());
        log.info("altText: {}, userId: {}, blogId: {}", altText, userId, blogId);
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 선택되지 않았습니다.");
            }

            // 이미지 파일 유효성 검사
            if (!isImageFile(file)) {
                return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다.");
            }

            // 파일 크기 제한 (10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("파일 크기가 10MB를 초과했습니다.");
            }

            String fileUrl = fileUploadService.uploadImage(file);
            log.info("Object Storage 업로드 완료: {}", fileUrl);
            
            // 데이터베이스에 미디어 정보 저장
            Integer actualBlogId = blogId != null ? blogId : 1; // blogId가 없으면 기본값 1 사용
            Integer mediaId = null;
            log.info("blogId 사용: 전달받은={}, 실제사용={}", blogId, actualBlogId);
            
            MediaDto mediaDto = MediaDto.builder()
                    .blogId(actualBlogId)
                    .uri(fileUrl)
                    .filename(file.getOriginalFilename())
                    .fileType("image")
                    .fileSize(Math.toIntExact(file.getSize()))
                    .build();
            log.info("MediaDto 생성 완료: {}", mediaDto);
            MediaDto savedMedia = mediaFileService.saveMedia(mediaDto);
            mediaId = savedMedia.getId();
            log.info("DB 저장 완료: mediaId={}", mediaId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("altText", altText != null ? altText : file.getOriginalFilename());
            response.put("mediaId", mediaId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping(value = "/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "altText", required = false) String altText,
                                       @RequestParam(value = "userId", required = false) Integer userId,
                                       @RequestParam(value = "blogId", required = false) Integer blogId) {
        log.info("=== 비디오 업로드 시작 ===");
        log.info("파일명: {}, 크기: {} bytes", file.getOriginalFilename(), file.getSize());
        log.info("altText: {}, userId: {}, blogId: {}", altText, userId, blogId);
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 선택되지 않았습니다.");
            }

            // 비디오 파일 유효성 검사
            if (!isVideoFile(file)) {
                return ResponseEntity.badRequest().body("비디오 파일만 업로드 가능합니다.");
            }

            // 파일 크기 제한 (50MB)
            if (file.getSize() > 50 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("파일 크기가 50MB를 초과했습니다.");
            }

            String fileUrl = fileUploadService.uploadVideo(file);
            log.info("Object Storage 업로드 완료: {}", fileUrl);
            
            // 데이터베이스에 미디어 정보 저장
            Integer actualBlogId = blogId != null ? blogId : 1; // blogId가 없으면 기본값 1 사용
            Integer mediaId = null;
            log.info("blogId 사용: 전달받은={}, 실제사용={}", blogId, actualBlogId);
            
            MediaDto mediaDto = MediaDto.builder()
                    .blogId(actualBlogId)
                    .uri(fileUrl)
                    .filename(file.getOriginalFilename())
                    .fileType("video")
                    .fileSize(Math.toIntExact(file.getSize()))
                    .build();
            log.info("MediaDto 생성 완료: {}", mediaDto);
            MediaDto savedMedia = mediaFileService.saveMedia(mediaDto);
            mediaId = savedMedia.getId();
            log.info("DB 저장 완료: mediaId={}", mediaId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("altText", altText != null ? altText : file.getOriginalFilename());
            response.put("mediaId", mediaId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "displayName", required = false) String displayName,
                                      @RequestParam(value = "userId", required = false) Integer userId,
                                      @RequestParam(value = "blogId", required = false) Integer blogId) {
        log.info("=== 파일 업로드 시작 ===");
        log.info("파일명: {}, 크기: {} bytes", file.getOriginalFilename(), file.getSize());
        log.info("displayName: {}, userId: {}, blogId: {}", displayName, userId, blogId);
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 선택되지 않았습니다.");
            }

            // 파일 크기 제한 (50MB)
            if (file.getSize() > 50 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("파일 크기가 50MB를 초과했습니다.");
            }

            String fileUrl = fileUploadService.uploadFile(file);
            log.info("Object Storage 업로드 완료: {}", fileUrl);
            
            // 데이터베이스에 미디어 정보 저장
            Integer actualBlogId = blogId != null ? blogId : 1; // blogId가 없으면 기본값 1 사용
            Integer mediaId = null;
            log.info("blogId 사용: 전달받은={}, 실제사용={}", blogId, actualBlogId);
            
            MediaDto mediaDto = MediaDto.builder()
                    .blogId(actualBlogId)
                    .uri(fileUrl)
                    .filename(displayName != null ? displayName : file.getOriginalFilename())
                    .fileType("file")
                    .fileSize(Math.toIntExact(file.getSize()))
                    .build();
            log.info("MediaDto 생성 완료: {}", mediaDto);
            MediaDto savedMedia = mediaFileService.saveMedia(mediaDto);
            mediaId = savedMedia.getId();
            log.info("DB 저장 완료: mediaId={}", mediaId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("fileName", displayName != null ? displayName : file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            response.put("fileType", getFileExtension(file.getOriginalFilename()));
            response.put("mediaId", mediaId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private boolean isVideoFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("video/");
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String extractFileNameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "";
        }
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
} 