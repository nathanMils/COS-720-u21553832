package com.project.server.conf;

import com.project.server.filter.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecureConf {
    private final AuthFilter authFilter;
    private final AuthenticationProvider authProvider;
    private static final String proxyPrefix = "/app/v1/";

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_MODERATOR \n ROLE_MODERATOR > ROLE_STUDENT \n ROLE_STUDENT > ROLE_APPLICANT";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

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
                                        .requestMatchers(proxyPrefix+"auth/**")
                                        .permitAll()
                                        .requestMatchers(proxyPrefix+"applicant/**")
                                        .hasRole("APPLICANT")
                                        .requestMatchers(proxyPrefix+"student/**")
                                        .hasRole("STUDENT")
                                        .requestMatchers(proxyPrefix+"moderator/**")
                                        .hasRole("MODERATOR")
                                        .requestMatchers(proxyPrefix+"admin/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(proxyPrefix+"module/{moduleId}/**")
                                        .hasAnyAuthority("module_{moduleId}_moderator","ADMIN")
                                        .requestMatchers(proxyPrefix+"module/{moduleId}/student/**")
                                        .hasAnyAuthority("module_{moduleId}_student","ADMIN")
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
