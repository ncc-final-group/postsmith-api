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
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    private int position;
    @Column(name = "is_child", nullable = false)
    private int isChild;
}
