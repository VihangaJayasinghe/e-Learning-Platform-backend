package com.Learn.ELP_backend.dto;

import lombok.Data;

@Data
public class TeacherRegisterDTO {
    private String username;
    private String nic;
    private String first_name;
    private String last_name;
    private String password;
    private String email;
    private String qualification;
    private Integer yearsOfExperience;
    private String subjectExpertise;
    private String bio;
    
}