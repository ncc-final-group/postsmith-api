package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentViewsEntity;

@Repository
public interface ContentViewsRepository extends JpaRepository<ContentViewsEntity, Integer> {
	 @Query("SELECT COALESCE(SUM(cv.viewsCount), 0) FROM ContentViewsEntity cv WHERE cv.content.id = :contentId")
	    int findTotalViewsByContentId(@Param("contentId") Integer contentId);
}