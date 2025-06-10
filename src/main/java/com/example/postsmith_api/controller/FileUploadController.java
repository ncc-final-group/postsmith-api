package com.example.postsmith_api.controller;

import com.example.postsmith_api.dto.media.MediaFileDto;
import com.example.postsmith_api.service.FileUploadService;
import com.example.postsmith_api.service.media.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api1/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final MediaFileService mediaFileService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "altText", required = false) String altText,
                                       @RequestParam(value = "userId", required = false) Integer userId,
                                       @RequestParam(value = "blogId", required = false) Integer blogId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 선택되지 않았습니다.");
            }

            // 이미지 파일 유효성 검사
            if (!isImageFile(file)) {
                return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다.");
            }

            // 파일 크기 제한 (2MB)
            if (file.getSize() > 2 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("파일 크기가 2MB를 초과했습니다.");
            }

            String fileUrl = fileUploadService.uploadImage(file);
            
            // 데이터베이스에 미디어 정보 저장
            if (userId != null) {
                MediaFileDto mediaFileDto = MediaFileDto.builder()
                        .fileName(extractFileNameFromUrl(fileUrl))
                        .originalFileName(file.getOriginalFilename())
                        .fileUrl(fileUrl)
                        .fileType("image")
                        .mimeType(file.getContentType())
                        .fileSize(file.getSize())
                        .altText(altText != null ? altText : file.getOriginalFilename())
                        .userId(userId)
                        .blogId(blogId)
                        .build();
                mediaFileService.saveMediaFile(mediaFileDto);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("altText", altText != null ? altText : file.getOriginalFilename());
            
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
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 선택되지 않았습니다.");
            }

            // 비디오 파일 유효성 검사
            if (!isVideoFile(file)) {
                return ResponseEntity.badRequest().body("비디오 파일만 업로드 가능합니다.");
            }

            // 파일 크기 제한 (8MB)
            if (file.getSize() > 8 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("파일 크기가 8MB를 초과했습니다.");
            }

            String fileUrl = fileUploadService.uploadVideo(file);
            
            // 데이터베이스에 미디어 정보 저장
            if (userId != null) {
                MediaFileDto mediaFileDto = MediaFileDto.builder()
                        .fileName(extractFileNameFromUrl(fileUrl))
                        .originalFileName(file.getOriginalFilename())
                        .fileUrl(fileUrl)
                        .fileType("video")
                        .mimeType(file.getContentType())
                        .fileSize(file.getSize())
                        .altText(altText != null ? altText : file.getOriginalFilename())
                        .userId(userId)
                        .blogId(blogId)
                        .build();
                mediaFileService.saveMediaFile(mediaFileDto);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("altText", altText != null ? altText : file.getOriginalFilename());
            
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
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 선택되지 않았습니다.");
            }

            // 파일 크기 제한 (5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("파일 크기가 5MB를 초과했습니다.");
            }

            String fileUrl = fileUploadService.uploadFile(file);
            
            // 데이터베이스에 미디어 정보 저장
            if (userId != null) {
                MediaFileDto mediaFileDto = MediaFileDto.builder()
                        .fileName(extractFileNameFromUrl(fileUrl))
                        .originalFileName(file.getOriginalFilename())
                        .fileUrl(fileUrl)
                        .fileType("file")
                        .mimeType(file.getContentType())
                        .fileSize(file.getSize())
                        .altText(displayName != null ? displayName : file.getOriginalFilename())
                        .userId(userId)
                        .blogId(blogId)
                        .build();
                mediaFileService.saveMediaFile(mediaFileDto);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("fileName", displayName != null ? displayName : file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            response.put("fileType", getFileExtension(file.getOriginalFilename()));
            
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