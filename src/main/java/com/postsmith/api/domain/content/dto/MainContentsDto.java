package com.postsmith.api.domain.content.dto;

import com.postsmith.api.entity.ContentsEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainContentsDto {

    private Integer id;
    private Integer blogId;
    private String title;
    private String contentPlain;
    private String thumbnail; // 썸네일 이미지 URL
    private Integer likes;
    private String contentHtml;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String blogAddress;
    private Integer sequence;

}