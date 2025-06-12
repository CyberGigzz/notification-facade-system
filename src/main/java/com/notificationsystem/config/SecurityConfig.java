package com.notificationsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF protection for now.
            // This is common for stateless REST APIs. We will re-evaluate for the Thymeleaf UI.
            .csrf(csrf -> csrf.disable())

            // 2. Define authorization rules
            .authorizeHttpRequests(authz -> authz
                // For now, permit all requests to any endpoint.
                // We will make this more restrictive later.
                .requestMatchers("/**").permitAll()
            )
            
            // 3. You can still configure basic auth or form login if you want a default
            // but the permitAll rule above makes it unnecessary for now.
            .httpBasic(withDefaults()); // Example of keeping a login mechanism configured

        return http.build();
    }
}