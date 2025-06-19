
package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.SubscriptionEntity;
import com.postsmith.api.entity.SubscriptionId;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, SubscriptionId> {
    @Query("SELECT COUNT(s) FROM SubscriptionEntity s WHERE s.blog.id = :blogId")
    int countByBlogId(@Param("blogId") Integer blogId);
}
