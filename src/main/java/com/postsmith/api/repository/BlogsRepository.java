package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    // Theme 정보를 fetch join으로 미리 로드
    @Query("SELECT b FROM BlogsEntity b LEFT JOIN FETCH b.theme WHERE b.id = :id")
    Optional<BlogsEntity> findByIdWithTheme(@Param("id") Integer id);
    
    @Query("SELECT b FROM BlogsEntity b LEFT JOIN FETCH b.theme WHERE b.address = :address")
    Optional<BlogsEntity> findByAddressWithTheme(@Param("address") String address);
}
