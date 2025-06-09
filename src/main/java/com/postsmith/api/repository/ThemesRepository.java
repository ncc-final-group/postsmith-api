package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ThemesEntity;

@Repository
public interface ThemesRepository extends JpaRepository<ThemesEntity, Integer> {
}
