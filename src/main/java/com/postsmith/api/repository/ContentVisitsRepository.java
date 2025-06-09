package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentVisitsEntity;

@Repository
public interface ContentVisitsRepository extends JpaRepository<ContentVisitsEntity, Integer> {
}
