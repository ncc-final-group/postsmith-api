package com.postsmith.api.domain.theme.service;

import com.postsmith.api.repository.UsersRepository;
import com.postsmith.api.domain.theme.dto.UsersDto;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {
	private final UsersRepository usersRepository;

	public List<UsersDto> getAllUsers() {
		return usersRepository.findAll().stream().map(UsersDto::fromEntity).collect(Collectors.toList());
	}
}
