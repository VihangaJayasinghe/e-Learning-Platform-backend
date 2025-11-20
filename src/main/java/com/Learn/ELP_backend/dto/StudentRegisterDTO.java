package com.Learn.ELP_backend.dto;

import lombok.Data;

@Data
public class StudentRegisterDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    
}