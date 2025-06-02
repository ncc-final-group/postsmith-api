package com.example.postsmith_api.repository.manage;

import com.example.postsmith_api.domain.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    public boolean existsByBlogUrl(String blogUrl);
    Optional<Blog> findByBlogUrl(String blogUrl);
}
