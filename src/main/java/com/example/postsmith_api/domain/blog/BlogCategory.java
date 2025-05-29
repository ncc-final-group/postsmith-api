package com.example.postsmith_api.domain.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(BlogCategoryId.class)
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BlogCategory {
    @Id
    @Column(name = "blog_id")
    private int blogId;
    @Id
    @Column(name = "category_id")
    private int categoryId;
}
