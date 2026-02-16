package com.Learn.ELP_backend.service;

import java.util.List;

import com.Learn.ELP_backend.model.User;

public interface AdminService {

    List<User> getAllUsers();
    User getuserByName(String username);
    User updateUser(String username,User user);
    void deleteUser(String username);
    List<User> getUserByRole(String role);
    List<User> searchUsers(String keyword);
    
    // System Stats
    com.Learn.ELP_backend.dto.SystemStatsDTO getSystemStats();
    
    // Course Management
    void deleteCourse(String courseId);
    com.Learn.ELP_backend.model.Course publishCourse(String courseId);
    List<com.Learn.ELP_backend.model.Course> getAllCourses();
    List<com.Learn.ELP_backend.model.Course> getCoursesByInstructor(String instructorUsername);

    // Class Management
    List<com.Learn.ELP_backend.model.Class> getAllClasses();
    void deleteClass(String classId); // Admin-level delete

}
