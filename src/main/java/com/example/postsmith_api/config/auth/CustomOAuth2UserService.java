package com.example.postsmith_api.config.auth;

import com.example.postsmith_api.config.auth.dto.*;
import com.example.postsmith_api.domain.Role;
import com.example.postsmith_api.domain.User;
import com.example.postsmith_api.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
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
            oAuth2Response = new GoogleAuthenticatinoDto(oAuth2User.getAttributes());
        }
        if(oAuth2Response == null) {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
        User user = userRepository.findByEmail(oAuth2Response.getEmail()).isPresent()?
                userRepository.findByEmail(oAuth2Response.getEmail()).get(): null;
        Role role = null;
        if(user == null) {
            user = User.builder()
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .picture(oAuth2Response.getAttributes().get("picture").toString())
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }else{
            role = user.getRole();
            user.setEmail(oAuth2Response.getEmail());
            userRepository.save(user);
        }
        return new CustomOAuth2User(oAuth2Response, role);
    }
}
