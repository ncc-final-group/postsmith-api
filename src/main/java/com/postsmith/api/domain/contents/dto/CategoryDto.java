package com.postsmith.api.domain.contents.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class CategoryDto {
    private Integer id;
    private String name;
    
    @JsonProperty("blog")
    private Integer blogId;

    private Integer parentId;
    
    private Integer sequence;
    private String description;
    private List<CategoryDto> children = new ArrayList<>();

    public CategoryDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }



}