package com.postsmith.api.theme.controller;

import com.postsmith.api.theme.dto.UsersDto;
import com.postsmith.api.theme.service.UsersService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3000"})
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public List<UsersDto> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<UsersDto> getByUuid(@PathVariable("uuid") String uuid) {
        return usersService.getUserByUuid(uuid);
    }
    
}
