package com.Learn.ELP_backend.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "users")
public class User {


    @Id
    private String id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String qualification;
    private String yearsOfExperience;
    private String subjectExpertise;
    private String department;
    private String bio;
    private String nic;
    private String AuthProvider;
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiry;
    private LocalDateTime lastpasswordChange;
 
}
