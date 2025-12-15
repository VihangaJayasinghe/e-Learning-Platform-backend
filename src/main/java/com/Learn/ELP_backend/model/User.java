package com.Learn.ELP_backend.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;
    private String nic;
    private String first_name;
    private String last_name;
    private String password;
    private String role;
    private String qualification;
    private Integer yearsOfExperience;
    private String subjectExpertise;
    private String bio;
    private String AuthProvider;

    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiry;
    private LocalDateTime lastPasswordChange;


public void setUsername(String username) {
        this.username = username;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

}
