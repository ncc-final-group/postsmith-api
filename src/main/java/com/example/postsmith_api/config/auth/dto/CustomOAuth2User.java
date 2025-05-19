package com.example.postsmith_api.config.auth.dto;

import com.example.postsmith_api.domain.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2Response oAuth2Response;
    private final Role role;

    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Response.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role.getKey();
            }
        });

        return collection;
    }
    public String getUsername(){
        return oAuth2Response.getProvider()+ "_" + oAuth2Response.getProviderId();
    }
}
