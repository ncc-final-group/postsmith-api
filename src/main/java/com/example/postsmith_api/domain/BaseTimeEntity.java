package com.example.postsmith_api.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @CreatedDate
    private Long createdAt;
    @CreatedDate
    private Long updatedAt;
}
