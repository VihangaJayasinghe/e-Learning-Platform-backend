package com.Learn.ELP_backend.controller;

import java.security.Principal;
import java.security.Provider.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Learn.ELP_backend.dto.ChangePasswordDTO;
import com.Learn.ELP_backend.dto.ForgotPasswordDTO;
import com.Learn.ELP_backend.dto.ResetPasswordDTO;
import com.Learn.ELP_backend.dto.StudentRegisterDTO;
import com.Learn.ELP_backend.dto.TeacherRegisterDTO;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register") //for tempory use
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.RegisterUser(user));
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterDTO dto) {
        return ResponseEntity.ok(userService.registerStudent(dto));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherRegisterDTO dto) {
        return ResponseEntity.ok(userService.registerTeacher(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.VerifyUser(user));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        try {
            userService.initiatePasswordReset(forgotPasswordDTO.getEmail());
            return ResponseEntity.ok("Password reset instructions sent to your email");
        } catch (Exception e) {
            return ResponseEntity.ok("Password reset instructions sent to your email");
            
        }
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            userService.resetPassword(resetPasswordDTO.getToken(), resetPasswordDTO.getNewPassword(), resetPasswordDTO.getConfirmPassword());
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid or expired reset token");
        }
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, Principal principal) {
        try {
            userService.changePassword(principal.getName(), changePasswordDTO.getCurrentPassword(), changePasswordDTO.getNewPassword());
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        boolean isValid = userService.validatePasswordResetToken(token);
        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }

}
