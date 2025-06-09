package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.BlogThemesEntity;

@Repository
public interface BlogThemesRepository extends JpaRepository<BlogThemesEntity, Integer> {
}
