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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecureConf {
    private final AuthFilter authFilter;
    private final AuthenticationProvider authProvider;
    private final LogoutService logoutService;
    private static final String proxyPrefix = "/app/v1/";

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
                .cors(
                        AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry ->
                                authorizationManagerRequestMatcherRegistry
                                        .requestMatchers(proxyPrefix+"public/**")
                                        .permitAll()
                                        .requestMatchers(proxyPrefix+"auth/**")
                                        .permitAll()
                                        .requestMatchers(proxyPrefix+"user/**")
                                        .authenticated()
                                        .requestMatchers(proxyPrefix+"student/**")
                                        .hasRole("STUDENT")
                                        .requestMatchers(proxyPrefix+"moduleModerator/**")
                                        .hasRole("MODULE_MODERATOR")
                                        .requestMatchers(proxyPrefix+"courseModerator/**")
                                        .hasRole("COURSE_MODERATOR")
                                        .requestMatchers(proxyPrefix+"admin/**")
                                        .hasRole("ADMIN")
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
                )
                .logout(
                        logout -> logout
                                .logoutUrl(proxyPrefix+"/user/logout")
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
