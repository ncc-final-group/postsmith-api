package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentViewsEntity;

@Repository
public interface ContentViewsRepository extends JpaRepository<ContentViewsEntity, Integer> {
}
