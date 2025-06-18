package com.postsmith.api.domain.manage.dto;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.MediaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MediaDto {
    private Integer id;
    private Integer blogId;
    private String uri;
    private String filename;
    private String fileType;
    private Integer fileSize;
    private LocalDateTime createdAt;

    @Builder
    public MediaDto(Integer id, Integer blogId, String uri, String filename,
                    String fileType, Integer fileSize, LocalDateTime createdAt) {
        this.id = id;
        this.blogId = blogId;
        this.uri = uri;
        this.filename = filename;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
    }

    public static MediaDto fromEntity(MediaEntity entity) {
        return MediaDto.builder()
                .id(entity.getId())
                .blogId(entity.getBlog().getId())
                .uri(entity.getUri())
                .filename(entity.getName())
                .fileType(entity.getType())
                .fileSize(entity.getSize())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public MediaEntity toEntity(BlogsEntity blog) {
        return MediaEntity.builder()
                .blog(blog)
                .uri(this.uri)
                .name(this.filename)
                .type(this.fileType)
                .size(this.fileSize)
                .build();
    }

    @Override
    public String toString() {
        return "MediaDto{" +
                "id=" + id +
                ", blogId=" + blogId +
                ", uri='" + uri + '\'' +
                ", filename='" + filename + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", createdAt=" + createdAt +
                '}';
    }
}