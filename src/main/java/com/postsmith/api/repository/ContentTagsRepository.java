package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentTagsEntity;
import com.postsmith.api.entity.ContentTagsId;

@Repository
public interface ContentTagsRepository extends JpaRepository<ContentTagsEntity, ContentTagsId> {
}
