package com.project.server.conf;

import com.project.server.filter.AuthFilter;
import com.project.server.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecureConf {
    private final AuthFilter authFilter;
    private final AuthenticationProvider authProvider;
    private final LogoutService logoutService;
    private static final String PREFIX = "/api/v1/";

    private final AuthenticationEntryPoint authEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_COURSE_MODERATOR \n ROLE_COURSE_MODERATOR > ROLE_MODULE_MODERATOR \n ROLE_MODULE_MODERATOR > ROLE_STUDENT";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry ->
                                authorizationManagerRequestMatcherRegistry
                                        .requestMatchers(PREFIX+"public/**")
                                        .permitAll()
                                        .requestMatchers(PREFIX+"auth/**")
                                        .permitAll()
                                        .requestMatchers(PREFIX+"user/**")
                                        .authenticated()
                                        .requestMatchers(PREFIX+"student/**")
                                        .hasRole("STUDENT")
                                        .requestMatchers(PREFIX+"moduleModerator/**")
                                        .hasRole("MODULE_MODERATOR")
                                        .requestMatchers(PREFIX+"courseModerator/**")
                                        .hasRole("COURSE_MODERATOR")
                                        .requestMatchers(PREFIX+"admin/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers("/elastic")
                                        .hasRole("ELASTIC")
                                        .anyRequest()
                                        .authenticated()
                )
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .exceptionHandling(
                        ex -> ex
                                .authenticationEntryPoint(authEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .authenticationProvider(
                        authProvider
                )
                .addFilterBefore(
                        authFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .logout(
                        logout -> logout
                                .logoutUrl(PREFIX+"/user/logout")
                                .addLogoutHandler(logoutService)
                                .logoutSuccessHandler(
                                        new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)
                                )
                                .clearAuthentication(false)
                                .permitAll()
                )
        ;
        return httpSecurity.build();
    }

}
