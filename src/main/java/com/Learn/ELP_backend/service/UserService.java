package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.dto.StudentRegisterDTO;
import com.Learn.ELP_backend.dto.TeacherRegisterDTO;
import com.Learn.ELP_backend.model.User;

public interface UserService {
    User RegisterUser(User user);//this temporary for register users with adding roles manually commonly used for register admin accounts
    User registerStudent(StudentRegisterDTO studentDTO);
    User registerTeacher(TeacherRegisterDTO teacherDTO);
    String VerifyUser(User user);
    void initiatePasswordReset(String email);
    boolean validatePasswordResetToken(String token);
    void resetPassword(String token, String newPassword,String confirmPassword);
    void changePassword(String username, String currentPassword, String newPassword);
    String generatePasswordResetToken();
    String generateOTP();
}
