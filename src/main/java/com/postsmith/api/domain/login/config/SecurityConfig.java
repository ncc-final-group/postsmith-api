package com.postsmith.api.domain.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.postsmith.api.domain.login.service.CustomOAuth2UserService;
import com.postsmith.api.domain.login.service.CustomOidcOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final OAuthSuccessHandler oAuthSuccessHandler;
	private final OAuthFailureHandler oAuthFailureHandler;
	private final CustomOAuth2UserService oAuthUserService;
	private final CustomOidcOAuth2UserService oidcOAuthUserService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll().anyRequest().authenticated());
		http.cors(Customizer.withDefaults());
		http.csrf(AbstractHttpConfigurer::disable);
		http.oauth2Login(oauth2 -> {
			oauth2.authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorize"));
			oauth2.userInfoEndpoint(userInfo -> {
				userInfo.userService(oAuthUserService);
				userInfo.oidcUserService(oidcOAuthUserService);
			});
			oauth2.successHandler(oAuthSuccessHandler);
			oauth2.failureHandler(oAuthFailureHandler);
		});
		http.logout(AbstractHttpConfigurer::disable);

		return http.build();
	}
}
