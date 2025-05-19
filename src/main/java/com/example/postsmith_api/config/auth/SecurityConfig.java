package com.example.postsmith_api.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/","/oauth2/**", "/auth/**","/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(Customizer.withDefaults())
                .oauth2Login(oauth2 -> oauth2.loginPage("/login").userInfoEndpoint(
                        userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/home")
                )
                //.formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
