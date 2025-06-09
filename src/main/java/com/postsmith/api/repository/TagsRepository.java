package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.TagsEntity;

@Repository
public interface TagsRepository extends JpaRepository<TagsEntity, Integer> {
}
