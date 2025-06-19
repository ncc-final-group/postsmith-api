package com.postsmith.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.UsersEntity;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
	Optional<UsersEntity> findByEmailAndProvider(String email, UsersEntity.ProviderEnum provider);
}
