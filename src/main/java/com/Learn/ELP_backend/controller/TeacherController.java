package com.Learn.ELP_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    @Autowired
    private com.Learn.ELP_backend.service.TeacherService teacherService;

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @GetMapping("/access")
    public String teacherAccess() {
        return "Teacher Access Granted.....";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/analytics")
    public org.springframework.http.ResponseEntity<com.Learn.ELP_backend.dto.TeacherAnalyticsDTO> getAnalytics(
            org.springframework.security.core.Authentication authentication) {
        String username = authentication.getName();
        return org.springframework.http.ResponseEntity.ok(teacherService.getAnalytics(username));
    }
}
