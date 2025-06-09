package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ThemeTagsEntity;
import com.postsmith.api.entity.ThemeTagsId;

@Repository
public interface ThemeTagsRepository extends JpaRepository<ThemeTagsEntity, ThemeTagsId> {
}
