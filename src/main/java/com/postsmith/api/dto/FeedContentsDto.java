package com.postsmith.api.dto;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.ContentsEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedContentsDto {
    private ContentsDto content;
    private BlogsDto blog;

    public static FeedContentsDto fromEntity(ContentsEntity contentEntity, BlogsEntity blogsEntity) {
        return FeedContentsDto.builder()
                .content(ContentsDto.fromEntity(contentEntity))
                .blog(BlogsDto.fromEntity(blogsEntity))
                .build();
    }
}
