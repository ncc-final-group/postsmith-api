package com.example.postsmith_api.config.auth.dto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NaverAuthenticationDto implements OAuth2Response {
    private final Map<String, Object> attributes;
    private HashMap<String, Object> response;
    public NaverAuthenticationDto(Map<String, Object> attributes) {
        this.attributes = attributes;
        response = (HashMap<String, Object>) attributes.get("response");
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
        return (String) response.get("email");
    }
    public String getProfileImage() {
        return (String) response.get("profile_image");
    }
    @Override
    public String getName() {
        return (String) response.get("name");
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
