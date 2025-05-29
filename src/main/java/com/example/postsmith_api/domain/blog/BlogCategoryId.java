package com.example.postsmith_api.domain.blog;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogCategoryId implements Serializable {
    @Column(name = "blog_id")
    private int blogId;
    @Column(name = "category_id")
    private int categoryId;
}
