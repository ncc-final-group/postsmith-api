package com.postsmith.api.domain.content.dto;

import com.postsmith.api.domain.feedContent.dto.ContentsDto;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.CategoriesEntity;
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
    private Integer categoryId;
    private Integer blogId;
    private String title;
    private String contentPlain;
    private Boolean isPublic;
    private Integer likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoryName;
    private String blogAddress;

    public static MainContentsDto MainContentsDtofromEntity(ContentsEntity entity) {
        return MainContentsDto.builder().
                id(entity.getId()).
                categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .blogId(entity.getBlog().getId())
                .blogAddress(entity.getBlog() != null ? entity.getBlog().getAddress() : null)
                .title(entity.getTitle())
                .contentPlain(entity.getContentPlain())
                .likes(entity.getLikes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ContentsEntity MainContentsDtotoEntity(CategoriesEntity category, BlogsEntity blog) {
        return ContentsEntity.builder()
                .category(category)
                .blog(blog)
                .title(this.title)
                .contentPlain(this.contentPlain)
                .isPublic(this.isPublic)
                .likes(this.likes)
                .build();
    }
}