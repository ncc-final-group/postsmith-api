package com.example.postsmith_api.domain.blog;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private int blogId;
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Column(name = "blog_name", nullable = false)
    private String blogName;
    @Column(name = "blog_url", nullable = false, unique = true)
    private String blogUrl;
    @Column(name = "blog_nickname", nullable = false)
    private String blogNickname;
    @Column(name = "category_id")
    @OneToMany(mappedBy = "blogId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> categoryList;
}
