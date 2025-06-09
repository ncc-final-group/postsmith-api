package com.example.postsmith_api.repository.manage;

import com.example.postsmith_api.domain.blog.Blogs;
import com.example.postsmith_api.domain.blog.Categories;
import com.example.postsmith_api.domain.blog.Contents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentsRepository extends JpaRepository<Contents, Integer> {
    boolean existsByCategoryId(Categories category);
    @Query("SELECT COALESCE(MAX(c.id), 0)+1 FROM Contents c WHERE c.blogId = ?1")
    int findLatestContentIdByBlogId(Blogs blogId);
}
