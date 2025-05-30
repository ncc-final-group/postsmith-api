package com.example.postsmith_api.repository.manage;

import com.example.postsmith_api.domain.blog.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Integer> {

}
