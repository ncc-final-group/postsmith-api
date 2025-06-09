package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.NotificationsEntity;

@Repository
public interface NotificationsRepository extends JpaRepository<NotificationsEntity, Integer> {
}
