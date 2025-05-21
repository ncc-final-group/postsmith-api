package com.example.postsmith_api.service.auth;

import com.example.postsmith_api.config.auth.dto.*;
import com.example.postsmith_api.domain.Role;
import com.example.postsmith_api.domain.User;
import com.example.postsmith_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String provider = "";
        OAuth2Response oAuth2Response = null;
        String profileImage = null;
        if(registrationId.equals("google")) {
            provider = "google";
            oAuth2Response = new GoogleAuthenticationDto(oAuth2User.getAttributes());
            oAuth2Response.getAttributes().get("picture");
        }else if(registrationId.equals("kakao")) {
            provider = "kakao";
            oAuth2Response = new KakaoAuthenticationDto(oAuth2User.getAttributes());
            profileImage = ((KakaoAuthenticationDto) oAuth2Response).getProfileImage();
        }else if(registrationId.equals("naver")) {
            provider = "naver";
            oAuth2Response = new NaverAuthenticationDto(oAuth2User.getAttributes());
            profileImage = ((NaverAuthenticationDto) oAuth2Response).getProfileImage();
        }
        if(oAuth2Response == null) {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
        Optional<User> optionalUser = userRepository.findByEmailAndProvider(oAuth2Response.getEmail(), provider);
        User user = optionalUser.orElse(null);
        Role role = null;
        if(user == null) {
            log.info("User not found, creating new user");
            user = User.builder()
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .picture(profileImage)
                    .role(Role.USER)
                    .provider(provider)
                    .build();
            System.out.println("User = " + user);
            userRepository.save(user);
        }else{
            log.info("User found, updating user");
            role = user.getRole();
            user.setName(oAuth2Response.getName());
            user.setPicture(profileImage);
            System.out.println("User = " + user);
            userRepository.save(user);
        }
        return new CustomOAuth2User(oAuth2Response, role);
    }
}
