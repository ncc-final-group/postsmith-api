package com.postsmith.api.dto;

import java.time.LocalDateTime;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.CategoriesEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.entity.ContentsEntity.ContentEnum;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.entity.UsersEntity.ProviderEnum;
import com.postsmith.api.entity.UsersEntity.RoleEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentsDto {
	private Integer id;
    private Integer categoryId;
    private Integer blogId;
    private Integer sequence;
    private ContentEnum type;
    private String title;
    private String contentHtml;
    private String contentPlain;
    private Boolean isTemp;
    private Boolean isPublic;
    private Integer likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ContentsDto fromEntity(ContentsEntity entity) {
        return ContentsDto.builder()
        		.id(entity.getId())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .blogId(entity.getBlog().getId())
                .sequence(entity.getSequence())
                .type(entity.getType())
                .title(entity.getTitle())
                .contentHtml(entity.getContentHtml())
                .contentPlain(entity.getContentPlain())
                .isTemp(entity.getIsTemp())
                .isPublic(entity.getIsPublic())
                .likes(entity.getLikes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ContentsEntity toEntity(CategoriesEntity category, BlogsEntity blog) {
        return ContentsEntity.builder()
                .category(category)
                .blog(blog)
                .sequence(this.sequence)
                .type(this.type)
                .title(this.title)
                .contentHtml(this.contentHtml)
                .contentPlain(this.contentPlain)
                .isTemp(this.isTemp)
                .isPublic(this.isPublic)
                .likes(this.likes)
                .build();
    }
}
