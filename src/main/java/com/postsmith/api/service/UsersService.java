package com.postsmith.api.service;

import com.postsmith.api.dto.UsersDto;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public List<UsersDto> getAllUsers() {
        return usersRepository.findAll().stream()
                .map(UsersDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<UsersDto> getUserByUuid(String uuid) {
        UsersEntity entity = usersRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UsersDto dto = UsersDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }
    
}
