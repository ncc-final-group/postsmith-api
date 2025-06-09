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
public class Blogs {
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int blogId;
    @Column(name = "blog_name", nullable = false)
    private String blogName;
    @Column(name = "address", nullable = false, unique = true)
    private String address;
    @Column(name = "blog_nickname", nullable = false)
    private String blogNickname;
    @Column(name = "category_id")
    @OneToMany(mappedBy = "blogId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Categories> categoryList;
}
