package com.project.server.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecureConf {
    private final AuthFilter authFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(
                        csrf -> csrf.disable()
                )
                .cors(
                        cors -> cors.disable()
                )
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry ->
                                authorizationManagerRequestMatcherRegistry
                                        .requestMatchers("/app/v1/auth/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .authenticationProvider(
                        authProvider
                )
                .addFilterBefore(
                        authFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return httpSecurity.build();
    }

}
