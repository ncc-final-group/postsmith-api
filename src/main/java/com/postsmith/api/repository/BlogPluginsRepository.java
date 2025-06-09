package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.BlogPluginsEntity;
import com.postsmith.api.entity.BlogPluginsId;

@Repository
public interface BlogPluginsRepository extends JpaRepository<BlogPluginsEntity, BlogPluginsId> {
}
