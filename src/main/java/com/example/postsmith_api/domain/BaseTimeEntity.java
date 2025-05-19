package com.example.postsmith_api.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public class BaseTimeEntity {
    private Long createdAt;
    private Long updatedAt;
}
