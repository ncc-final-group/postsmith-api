package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.UsersEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogsRepository extends JpaRepository<BlogsEntity, Integer> {
    boolean existsByAddress(String address);
    Optional<BlogsEntity> findByAddress(String address);
    List<BlogsEntity> findByUserOrderByCreatedAtDesc(UsersEntity user);
    Long countByUser(UsersEntity user);
}
