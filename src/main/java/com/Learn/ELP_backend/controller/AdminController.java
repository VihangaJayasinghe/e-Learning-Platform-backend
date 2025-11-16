package com.Learn.ELP_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/access")
    public String adminAccess() {
        return "Admin Access Granted.....";
    }

}
