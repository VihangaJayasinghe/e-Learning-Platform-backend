package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Enrollment;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.UserRepository;
import com.Learn.ELP_backend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/enroll/{classId}")
    public ResponseEntity<?> enrollStudent(@PathVariable String classId, Principal principal) {
        String username = principal.getName();
        User student = userRepository.findByUsername(username);

        try {
            Enrollment enrollment = enrollmentService.enrollStudent(student, classId);
            return ResponseEntity.ok(enrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my-enrollments")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(Principal principal) {
        String username = principal.getName();
        User student = userRepository.findByUsername(username);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student);
        return ResponseEntity.ok(enrollments);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/check/{classId}")
    public ResponseEntity<Boolean> checkEnrollment(@PathVariable String classId, Principal principal) {
        String username = principal.getName();
        User student = userRepository.findByUsername(username); // Determine student from principal
        boolean isEnrolled = enrollmentService.isEnrolled(student.getId(), classId);
        return ResponseEntity.ok(isEnrolled);
    }
}
