package com.Learn.ELP_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    // Load email configuration from application.properties
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.base-url:http://localhost:9090}")
    private String baseUrl;
    
    @Value("${app.company-name:Project Security}")
    private String companyName;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetLink = baseUrl + "/reset-password?token=" + resetToken;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText(
            "Hello,\n\n" +
            "You requested to reset your password. Click the link below:\n" +
            resetLink + "\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "If you didn't request this, please ignore this email.\n\n" +
            "Best regards,\n" +
            companyName + " Team"
        );
        
        mailSender.send(message);
    }

    // Method to send OTP email for password reset verification (not implemented in controller yet))
    public void sendOTPEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your Verification Code");
        message.setText(
            "Hello,\n\n" +
            "Your verification code is: " + otp + "\n\n" +
            "This code will expire in 15 minutes.\n\n" +
            "If you didn't request this code, please ignore this email.\n\n" +
            "Best regards,\n" +
            companyName + " Team"
        );
        
        mailSender.send(message);
    }
}