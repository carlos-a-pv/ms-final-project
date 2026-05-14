package com.example.jwt_security.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("securityFilterChain method got called..");

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                            auth
                                    .requestMatchers("/api/v1/auth/**").permitAll()
                                    .requestMatchers("/api/v1/admin/**").hasAuthority("ROLE_ADMIN")
                                    .requestMatchers("/api/v1/customer/**").hasAuthority("ROLE_CUSTOMER")
                                    .anyRequest().authenticated();
                        }
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

