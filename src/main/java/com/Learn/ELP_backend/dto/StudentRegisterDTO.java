package com.Learn.ELP_backend.dto;

import lombok.Data;

@Data
public class StudentRegisterDTO {
    private String username;
    private String first_name;
    private String last_name;
    private String password;
    private String email;
    
}