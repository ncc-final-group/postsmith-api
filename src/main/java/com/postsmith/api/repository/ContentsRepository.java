package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentsEntity;

@Repository
public interface ContentsRepository extends JpaRepository<ContentsEntity, Integer> {
}
