package com.postsmith.api.domain.theme.controller;

import com.postsmith.api.domain.theme.dto.UsersDto;
import com.postsmith.api.domain.theme.service.UsersService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

	private final UsersService usersService;

	@GetMapping
	public List<UsersDto> getAllUsers() {
		return usersService.getAllUsers();
	}
}
