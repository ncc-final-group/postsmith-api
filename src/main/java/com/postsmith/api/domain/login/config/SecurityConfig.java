package com.postsmith.api.domain.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.postsmith.api.domain.login.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomCorsConfigurationSource corsConfigurationSource;
	private final OAuthSuccessHandler oAuthSuccessHandler;
	private final CustomOAuth2UserService oAuthUserService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//        .csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(authz -> authz
//            .requestMatchers("/api/user").authenticated()
//            .anyRequest().permitAll()
//        )
//        .oauth2Login(oauth2 -> oauth2
//            .defaultSuccessUrl("http://localhost:3000/oauth-success", true)
//        );
		http.cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource));
		http.csrf(Customizer.withDefaults());
		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);
		http.oauth2Login(oauth2 -> {
			oauth2.loginPage("/oauth2/authorization/postsmith");
			oauth2.successHandler(oAuthSuccessHandler);
			oauth2.defaultSuccessUrl("/api/v1/auth/oauth2/success", true);
			oauth2.failureUrl("/api/v1/auth/oauth2/failure");
			oauth2.authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorize"));
			oauth2.userInfoEndpoint(userInfo -> userInfo.userService(oAuthUserService));
//			oauth2.authorizeRequests(authz -> authz
//	                .requestMatchers("/api/user").authenticated()
//	                .anyRequest().permitAll());
//
		});

		return http.build();
	}
}
