package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.MediaEntity;

@Repository
public interface UploadsRepository extends JpaRepository<MediaEntity, Integer> {
}
