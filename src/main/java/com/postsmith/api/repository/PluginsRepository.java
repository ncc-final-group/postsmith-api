package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.PluginsEntity;

@Repository
public interface PluginsRepository extends JpaRepository<PluginsEntity, Integer> {
}
