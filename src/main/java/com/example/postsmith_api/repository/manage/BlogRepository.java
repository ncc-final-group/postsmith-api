package com.example.postsmith_api.repository.manage;

import com.example.postsmith_api.domain.blog.Blogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blogs, Integer> {
    public boolean existsByAddress(String address);
    Optional<Blogs> findByAddress(String address);
}
