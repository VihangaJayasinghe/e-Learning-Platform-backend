package com.Learn.ELP_backend.service.impl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Learn.ELP_backend.dto.StudentRegisterDTO;
import com.Learn.ELP_backend.dto.TeacherRegisterDTO;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.UserRepository;
import com.Learn.ELP_backend.service.EmailService;
import com.Learn.ELP_backend.service.JWTService;
import com.Learn.ELP_backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder(12);
    //this method is temporary for register users with adding roles commonly used for register admin accounts
    @Override
    public User RegisterUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public String VerifyUser(User user) {
        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
            )
        );
        if (authentication.isAuthenticated()) {
            return jwtService.GenerateToken(user);
        } else {
            return "Authentication failed";
        }
    }
    @Override
    public User registerStudent(StudentRegisterDTO studentDTO) {
        if (userRepository.findByUsername(studentDTO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(studentDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(studentDTO.getUsername());
        user.setFirstname(studentDTO.getFirstname());
        user.setLastname(studentDTO.getLastname());
        user.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        user.setEmail(studentDTO.getEmail());
        user.setRole("STUDENT");
        user.setAuthProvider("local");
        
        return userRepository.save(user);
    }
    @Override
    public User registerTeacher(TeacherRegisterDTO teacherDTO) {
        // Check if user already exists
        if (userRepository.findByUsername(teacherDTO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(teacherDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // Convert DTO to entity
        User user = new User();
        user.setUsername(teacherDTO.getUsername());
        user.setFirstname(teacherDTO.getFirstname());
        user.setLastname(teacherDTO.getLastname());
        user.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
        user.setEmail(teacherDTO.getEmail());
        user.setRole("TEACHER");
        user.setAuthProvider("local");
        
        // Set teacher-specific fields
        user.setQualification(teacherDTO.getQualification());
        user.setYearsOfExperience(teacherDTO.getYearsOfExperience());
        user.setSubjectExpertise(teacherDTO.getSubjectExpertise());
        user.setBio(teacherDTO.getBio());

        return userRepository.save(user);
    }
    @Override
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String resetToken = generatePasswordResetToken();
            user.setPasswordResetToken(resetToken);
            user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(24));
            userRepository.save(user);
            
            // Send email with reset token
            emailService.sendPasswordResetEmail(email, resetToken);
        }
        
    }
    @Override
    public boolean validatePasswordResetToken(String token) {
        User user = userRepository.findByPasswordResetToken(token);
        return user != null && 
               user.getPasswordResetTokenExpiry() != null &&
               user.getPasswordResetTokenExpiry().isAfter(LocalDateTime.now());
    }
    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token);
        if (user != null && validatePasswordResetToken(token)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiry(null);
            user.setLastPasswordChange(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid or expired reset token");
        }
    }
    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setLastPasswordChange(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new RuntimeException("Current password is incorrect");
        }
    }
    @Override
    public String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }
    @Override
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    

}
