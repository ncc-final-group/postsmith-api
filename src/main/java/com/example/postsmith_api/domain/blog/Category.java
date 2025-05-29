package com.example.postsmith_api.domain.blog;

import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;
}
