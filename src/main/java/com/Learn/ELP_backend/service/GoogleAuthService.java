package com.Learn.ELP_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.UserRepository;

import java.util.Map;

@Service
public class GoogleAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    public String processGoogleUser(OAuth2AuthenticationToken authentication) {
        // Extract user info from Google
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        // Check if user exists by email
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            // Auto-register new student
            user = new User();
            user.setUsername(name); // Use Google name as username
            user.setEmail(email);
            user.setPassword("GOOGLE_OAUTH"); // Dummy password since they use Google login
            user.setRole("STUDENT"); // Auto-assign as student
            user.setAuthProvider("GOOGLE");
            user = userRepository.save(user);
        }

        // Generate JWT token (same as your regular login)
        return jwtService.GenerateToken(user);
    }
}