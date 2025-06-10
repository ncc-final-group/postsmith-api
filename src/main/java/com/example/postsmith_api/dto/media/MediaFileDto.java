package com.example.postsmith_api.dto.media;

import com.example.postsmith_api.domain.media.MediaFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MediaFileDto {
    
    private Long id;
    private String fileName;
    private String originalFileName;
    private String fileUrl;
    private String fileType;
    private String mimeType;
    private Long fileSize;
    private String altText;
    private String description;
    private Integer userId;
    private Integer blogId;
    private LocalDateTime createdAt;
    
    @Builder
    public MediaFileDto(Long id, String fileName, String originalFileName, String fileUrl,
                       String fileType, String mimeType, Long fileSize, String altText,
                       String description, Integer userId, Integer blogId, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.altText = altText;
        this.description = description;
        this.userId = userId;
        this.blogId = blogId;
        this.createdAt = createdAt;
    }
    
    // Entity to DTO
    public static MediaFileDto fromEntity(MediaFile mediaFile) {
        return MediaFileDto.builder()
                .id(mediaFile.getId())
                .fileName(mediaFile.getFileName())
                .originalFileName(mediaFile.getOriginalFileName())
                .fileUrl(mediaFile.getFileUrl())
                .fileType(mediaFile.getFileType())
                .mimeType(mediaFile.getMimeType())
                .fileSize(mediaFile.getFileSize())
                .altText(mediaFile.getAltText())
                .description(mediaFile.getDescription())
                .userId(mediaFile.getUserId())
                .blogId(mediaFile.getBlogId())
                .createdAt(mediaFile.getCreatedAt())
                .build();
    }
    
    // DTO to Entity
    public MediaFile toEntity() {
        return MediaFile.builder()
                .fileName(this.fileName)
                .originalFileName(this.originalFileName)
                .fileUrl(this.fileUrl)
                .fileType(this.fileType)
                .mimeType(this.mimeType)
                .fileSize(this.fileSize)
                .altText(this.altText)
                .description(this.description)
                .userId(this.userId)
                .blogId(this.blogId)
                .build();
    }
} 