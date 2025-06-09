package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.BlogsEntity;

@Repository
public interface BlogsRepository extends JpaRepository<BlogsEntity, Integer> {
}
