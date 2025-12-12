package com.Learn.ELP_backend.service.impl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Learn.ELP_backend.dto.StudentRegisterDTO;
import com.Learn.ELP_backend.dto.TeacherRegisterDTO;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.UserRepository;
import com.Learn.ELP_backend.service.EmailService;
import com.Learn.ELP_backend.service.JWTService;
import com.Learn.ELP_backend.service.UserService;
import com.Learn.ELP_backend.exceptions.UserAlreadyExistsException;

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

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    // Register admin or manual user creation for tempory use
    @Override
    public User RegisterUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()) ||
            userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public String VerifyUser(User user) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return jwtService.GenerateToken(user);
        } else {
            return "Authentication failed";
        }
    }

    // Register Student
    @Override
    public User registerStudent(StudentRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()) ||
            userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        User student = new User();
        student.setUsername(dto.getUsername());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setEmail(dto.getEmail());
        student.setRole("STUDENT");
        student.setAuthProvider("local");

        return userRepository.save(student);
    }

    // Register Teacher
    @Override
    public User registerTeacher(TeacherRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()) ||
            userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        User teacher = new User();
        teacher.setUsername(dto.getUsername());
        teacher.setPassword(passwordEncoder.encode(dto.getPassword()));
        teacher.setEmail(dto.getEmail());
        teacher.setRole("TEACHER");
        teacher.setAuthProvider("local");

        teacher.setQualification(dto.getQualification());
        teacher.setYearsOfExperience(dto.getYearsOfExperience());
        teacher.setSubjectExpertise(dto.getSubjectExpertise());
        teacher.setBio(dto.getBio());

        return userRepository.save(teacher);
    }

    @Override
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String resetToken = generatePasswordResetToken();
            user.setPasswordResetToken(resetToken);
            user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(24));
            userRepository.save(user);
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
