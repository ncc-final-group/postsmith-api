package com.example.postsmith_api.domain.blog;

import com.example.postsmith_api.domain.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "contents")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contents extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "blog_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Blogs blogId;
    @Column(name = "sequence")
    private int sequence;
    @JoinColumn(name = "category_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Categories categoryId;
    @Column(name = "type", nullable = false)
    private PostType postType;
    private String title;
    @Column(name = "content_html", columnDefinition = "TEXT")
    private String contentHtml;
    @Column(name = "content_plain", columnDefinition = "TEXT")
    private String contentPlain;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}
