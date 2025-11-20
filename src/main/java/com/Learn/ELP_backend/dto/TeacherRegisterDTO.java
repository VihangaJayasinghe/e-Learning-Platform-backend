package com.Learn.ELP_backend.dto;

import lombok.Data;

@Data
public class TeacherRegisterDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String qualification;
    private Integer yearsOfExperience;
    private String subjectExpertise;
    private String bio;
    
}