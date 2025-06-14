package com.notificationsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Define authorization rules
            .authorizeHttpRequests(authz -> authz
                // Allow access to static resources (CSS, JS) for everyone
                .requestMatchers("/css/**", "/js/**").permitAll()
                // Allow access to the API endpoints for other systems (we'll secure this differently later if needed)
                .requestMatchers("/api/**").permitAll()
                // All other requests under /admin/** must be authenticated
                .requestMatchers("/admin/**").authenticated()
                // Any other request not matched above is permitted (e.g., a future homepage)
                .anyRequest().permitAll()
            )
            
            // 2. Configure the login form
            .formLogin(form -> form
                // Specify the URL for our custom login page
                .loginPage("/login")
                // Specify the URL where the login form will POST to for processing
                .loginProcessingUrl("/login")
                // Specify the URL to redirect to on successful login
                .defaultSuccessUrl("/admin/customers", true)
                // Allow everyone to access the login page
                .permitAll()
            )
            
            // 3. Configure logout
            .logout(logout -> logout
                // Specify the URL to trigger logout
                .logoutUrl("/logout")
                // Specify the URL to redirect to after logout
                .logoutSuccessUrl("/login?logout")
                // Invalidate the session and clear authentication
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )

            // 4. Disable CSRF for API, but it's generally good for form-based UIs.
            // We'll keep it simple for now.
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use BCrypt for strong, salted password hashing
        return new BCryptPasswordEncoder();
    }
}