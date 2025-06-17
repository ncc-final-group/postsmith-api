package com.postsmith.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MediaEntitiy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uri", nullable = false, length = 255)
    private String uri;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "size")
    private Integer size;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private BlogsEntity blog;

}

