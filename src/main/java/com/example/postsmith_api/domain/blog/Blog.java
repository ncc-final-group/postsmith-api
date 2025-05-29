package com.example.postsmith_api.domain.blog;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private int blogId;
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Column(name = "blog_category_id")
    private List<Integer> blogCategoryId;

}
