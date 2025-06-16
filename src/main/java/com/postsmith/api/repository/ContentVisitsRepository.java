package com.postsmith.api.repository;

import com.postsmith.api.entity.ContentViewsEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.entity.UsersEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentVisitsEntity;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ContentVisitsRepository extends JpaRepository<ContentVisitsEntity, Integer> {
    Optional<ContentVisitsEntity> findByUser(UsersEntity user);
    @Query("SELECT cv FROM ContentVisitsEntity cv WHERE cv.user = :user AND DATE(cv.createdAt) = :createdOn")
    Optional<ContentVisitsEntity> findByUserAndCreatedOn(@Param("user") UsersEntity user, @Param("createdOn") LocalDate createdOn);
    @Query("SELECT cv FROM ContentVisitsEntity cv WHERE cv.ip = :ipAddress AND DATE(cv.createdAt) = :now")
    Optional<ContentVisitsEntity> findByIpAndCreatedOn(@Param("user") String ipAddress, @Param("now") LocalDate now);
}
