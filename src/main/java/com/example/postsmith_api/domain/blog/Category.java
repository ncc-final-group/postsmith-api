package com.example.postsmith_api.domain.blog;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "blog_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Blog blogId;
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    @Column(name = "parent_category_id")
    private int parentCategoryId;
    @Column(name = "is_child", nullable = false)
    private int isChild;
}
