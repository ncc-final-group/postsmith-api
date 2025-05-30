package com.example.postsmith_api.domain.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class BlogCategoryId implements Serializable {
    @Column(name = "blog_id")
    private int blogId;
    @Column(name = "category_id")
    private int categoryId;
}
