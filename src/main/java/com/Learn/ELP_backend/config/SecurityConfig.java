package com.Learn.ELP_backend.config;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(customizer -> customizer.disable()) // Disable CSRF
            .cors(customizer -> customizer.configurationSource(corsConfigurationSource())) // Enable CORS with our config
            .authorizeHttpRequests(request -> request
                .requestMatchers(
                    "/api/users/register",
                    "/api/users/register/student",
                    "/api/users/register/teacher",
                    "/api/users/login",
                    "/api/users/forgot-password",
                    "/api/users/reset-password",
                    "/api/users/validate-reset-token",
                    "/auth/**",
                    "/login/**",
                    "/oauth2/**",
                    "/v3/api-docs/**", 
                    "/swagger-ui/**", 
                    "/swagger-ui.html"
                ).permitAll() // Allow these without token
                .anyRequest().authenticated() // Block everything else
            )
            .httpBasic(Customizer.withDefaults())
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    // 1. Process the Google User (Create in DB if new)
                    String jwtToken = googleAuthService.processGoogleUser((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication);
                    
                    // 2. Create the Secure Cookie
                    jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", jwtToken);
                    cookie.setHttpOnly(true);
                    cookie.setSecure(false); // Set to true if using HTTPS
                    cookie.setPath("/");
                    cookie.setMaxAge(24 * 60 * 60);
                    response.addCookie(cookie);

                    // 3. REDIRECT back to React (instead of sending JSON)
                    // We send them to a special "loading" page to finish the login
                    response.sendRedirect("http://localhost:5173/google-callback");
                })
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("http://localhost:5173/login?error=google_failed");
                })
            )
            .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Allow React
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow Cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }
}