package com.Learn.ELP_backend.service;


import org.springframework.stereotype.Service;

@Service
public class AdminSecrity {
    
    public void validateKey(String key) {
        // Check for null or empty key
        if (key == null || key.trim().isEmpty()) {
            throw new RuntimeException("Security key cannot be empty");
        }
        
        // Validate against your secret key
        if (!"MY_ADMIN_2025_SECRET".equals(key)) {
            throw new RuntimeException("Invalid security key provided");
        }
    }
}
