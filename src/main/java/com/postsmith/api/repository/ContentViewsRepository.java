package com.postsmith.api.repository;

import com.postsmith.api.domain.manage.dto.ContentViewsDto;
import com.postsmith.api.entity.ContentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentViewsEntity;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ContentViewsRepository extends JpaRepository<ContentViewsEntity, Integer> {
    Optional<ContentViewsEntity> findByContentAndCreatedOn(ContentsEntity content, LocalDate createdOn);
    
    @Query("SELECT COALESCE(SUM(cv.viewsCount), 0) FROM ContentViewsEntity cv WHERE cv.content = :content")
    Integer getTotalViewsByContent(@Param("content") ContentsEntity content);
}
