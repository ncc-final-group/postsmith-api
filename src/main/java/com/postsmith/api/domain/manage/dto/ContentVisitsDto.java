package com.postsmith.api.domain.manage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContentVisitsDto {
    private Integer id;
    private Integer contentId; // FK > contents.id
    private Integer userId; // FK > users.id
    private String ipAddress; // IP address of the visitor
    private LocalDateTime createdAt; // Timestamp of the visit
}
