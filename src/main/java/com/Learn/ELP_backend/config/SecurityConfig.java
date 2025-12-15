package com.Learn.ELP_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Learn.ELP_backend.filter.JWTFilter;
import com.Learn.ELP_backend.service.GoogleAuthService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JWTFilter jwtfilter;

    @Autowired
    private GoogleAuthService googleAuthService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                    .requestMatchers("/api/users/register","/api/users/register/student","/api/users/register/teacher", "/api/users/login","/api/users/forgot-password","/api/users/reset-password","/api/users/validate-reset-token","/auth/**", "/login/**", "/oauth2/**").permitAll()
                    .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(oauth2 -> oauth2
                    .successHandler((request, response, authentication) -> {
                        // Handle successful OAuth2 login
                        String jwtToken = googleAuthService.processGoogleUser((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication);
                        
                        // Return JSON response instead of redirecting
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        String jsonResponse = String.format(
                            "{\"token\": \"%s\", \"message\": \"Google login successful\"}",
                            jwtToken
                        );
                        response.getWriter().write(jsonResponse);
                        response.getWriter().flush();
                    })
                    .failureHandler((request, response, exception) -> {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        String jsonResponse = "{\"message\": \"Google authentication failed\"}";
                        response.getWriter().write(jsonResponse);
                        response.getWriter().flush();
                    })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }
}