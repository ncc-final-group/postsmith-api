package com.postsmith.api.domain.manage.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ContentViewsDto {
    private Integer id;
    private Integer contentId;
    private Integer viewsCount;
    private LocalDate createdOn;
}
