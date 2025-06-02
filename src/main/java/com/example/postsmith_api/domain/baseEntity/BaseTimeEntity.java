package com.example.postsmith_api.domain.baseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @CreatedDate
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
    @CreatedDate
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}
