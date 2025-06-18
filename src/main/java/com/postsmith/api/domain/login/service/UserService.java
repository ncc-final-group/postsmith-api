package com.postsmith.api.domain.login.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.postsmith.api.domain.login.dto.UserCreateDto;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UsersRepository userRepository;
	private final BcryptService bcryptService;

	public void createUser(UserCreateDto userCreateDto) {
		String uuid = UUID.randomUUID().toString();
		String email = userCreateDto.getEmail();
		String password = bcryptService.encodeBcrypt(uuid, 10); // 임시 비밀번호
		UsersEntity.ProviderEnum provider = UsersEntity.ProviderEnum.valueOf(userCreateDto.getProvider().toUpperCase());
		UsersEntity.RoleEnum role = UsersEntity.RoleEnum.valueOf(userCreateDto.getRole().toUpperCase());
		String nickname = userCreateDto.getNickname();
		UsersEntity user = UsersEntity.builder().uuid(uuid).email(email).password(password).provider(provider).role(role).nickname(nickname).build();
		userRepository.save(user);
	}

	public UsersEntity readUserByEmail(String email, UsersEntity.ProviderEnum provider) {
		if (email == null || email.isEmpty()) {
			return null;
		}

		return userRepository.findByEmailAndProvider(email, provider).orElse(null);
	}
}
