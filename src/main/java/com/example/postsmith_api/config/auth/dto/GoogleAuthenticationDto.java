package com.example.postsmith_api.config.auth.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleAuthenticationDto implements OAuth2Response {
    private final Map<String, Object> attributes;
    public GoogleAuthenticationDto(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
