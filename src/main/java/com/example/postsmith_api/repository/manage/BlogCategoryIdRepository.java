package com.example.postsmith_api.repository.manage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogCategoryIdRepository extends JpaRepository<Integer, Integer> {
    List<Integer> findByBlogId(Integer blogId);
}
