package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.repository.UserRepository;
import java.util.Map;
import java.util.HashMap;

import java.security.Principal;
import java.security.Provider.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
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

    /*@PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.VerifyUser(user));
    }*/

   @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) {
        // 1. Authenticate
        String token = userService.VerifyUser(user);
        
        if (token.equals("Authentication failed")) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // 2. Set the Secure Cookie (Token stays hidden here)
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        // 3. Get User Details (So React knows who this is)
        User fullUser = userRepository.findByUsername(user.getUsername());

        // 4. Send JSON response with Name and Role
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful");
        responseBody.put("username", fullUser.getUsername());
        responseBody.put("role", fullUser.getRole());

        return ResponseEntity.ok(responseBody);
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
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("Not authenticated");
        
        User user = userRepository.findByUsername(principal.getName());
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("email", user.getEmail());
        
        // If it's a teacher, you might have these extra fields
        response.put("bio", user.getBio()); 
        response.put("qualification", user.getQualification());
        
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update-profile")
public ResponseEntity<?> updateProfile(@RequestBody User updatedData, Principal principal) {
    User user = userRepository.findByUsername(principal.getName());
    if (user == null) return ResponseEntity.status(404).body("User not found");

    // Update only allowed fields
    user.setEmail(updatedData.getEmail());
    user.setBio(updatedData.getBio());
    
    if ("TEACHER".equals(user.getRole())) {
        user.setQualification(updatedData.getQualification());
    }

    userRepository.save(user);
    return ResponseEntity.ok("Profile updated successfully");
}

}
