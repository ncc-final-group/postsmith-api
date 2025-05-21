package com.example.postsmith_api.config.auth.dto;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class KakaoAuthenticationDto implements OAuth2Response {
    private final Map<String, Object> attributes;
    public KakaoAuthenticationDto(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Object object = attributes.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) object;
        return (String) accountMap.get("email");
    }
    public String getProfileImage() {
        Object object = attributes.get("properties");
        LinkedHashMap accountMap = (LinkedHashMap) object;
        return (String) accountMap.get("profile_image");
    }
    @Override
    public String getName() {
        return "test";
        // return attributes.get("name").toString();
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
