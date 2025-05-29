package com.example.postsmith_api.domain.blog;

import com.example.postsmith_api.domain.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "posts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "blog_id")
    private int blogId;
    @Column(name = "category_id")
    private int category_id;
    private String title;
    @Column(name = "content_html")
    private String contentHtml;
    @Column(name = "content_preview")
    private String contentPreview;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
    private int views;
    private int likes;
}
