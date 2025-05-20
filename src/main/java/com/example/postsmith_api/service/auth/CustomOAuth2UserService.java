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

        OAuth2Response oAuth2Response = null;

        if(registrationId.equals("google")) {
            oAuth2Response = new GoogleAuthenticationDto(oAuth2User.getAttributes());
        }
        if(oAuth2Response == null) {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
        User user = userRepository.findByEmail(oAuth2Response.getEmail()).isPresent()?
                userRepository.findByEmail(oAuth2Response.getEmail()).get(): null;
        Role role = null;
        if(user == null) {
            log.info("User not found, creating new user");
            user = User.builder()
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .picture(oAuth2Response.getAttributes().get("picture").toString())
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }else{
            log.info("User found, updating user");
            role = user.getRole();
            user.setName(oAuth2Response.getName());
            user.setPicture(oAuth2Response.getAttributes().get("picture").toString());
            userRepository.save(user);
        }
        return new CustomOAuth2User(oAuth2Response, role);
    }
}
