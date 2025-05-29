package com.example.postsmith_api.repository.blog;

import com.example.postsmith_api.domain.blog.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Integer> {
    boolean existsByCategory(String category);
}
