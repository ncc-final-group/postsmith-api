package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.UploadsEntity;

@Repository
public interface UploadsRepository extends JpaRepository<UploadsEntity, Integer> {
}
