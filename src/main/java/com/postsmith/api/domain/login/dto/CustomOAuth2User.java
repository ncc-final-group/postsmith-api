/*
package com.postsmith.api.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.postsmith.api.entity.UsersEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2Response oAuth2Response;
    private final UsersEntity.RoleEnum role;

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
*/
