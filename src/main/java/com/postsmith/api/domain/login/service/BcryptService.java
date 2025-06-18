package com.postsmith.api.domain.login.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptService {
	public String encodeBcrypt(String planeText, int strength) {
		return new BCryptPasswordEncoder(strength).encode(planeText);
	}

	public Boolean matchesBcrypt(String planeText, String hashValue, int strength) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(strength);
		return passwordEncoder.matches(planeText, hashValue);
	}
}
