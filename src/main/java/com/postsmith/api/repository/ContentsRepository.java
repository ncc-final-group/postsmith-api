package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentsEntity;

import java.util.List;

@Repository
public interface ContentsRepository extends JpaRepository<ContentsEntity, Integer> {
    List<ContentsEntity> findByBlog_IdAndType(Integer blogId, ContentsEntity.ContentEnum type);
}
