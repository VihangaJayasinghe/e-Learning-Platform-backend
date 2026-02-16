package com.Learn.ELP_backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.UserRepository;
import com.Learn.ELP_backend.service.AdminService;


@Service

public class AdminServiceImpl implements AdminService{


    @Autowired
    private UserRepository userRepository;

@Override
public List<User> getAllUsers() {
    return userRepository.findAll();
}


@Override
public User getuserByName(String username) {
    User user=userRepository.findByUsernameContainingIgnoreCase(username);

 if (user == null) {
        throw new RuntimeException("User not found with username: " + username);
    }

    return user;
  }



    @Override
    public User updateUser(String username, User user) {
     
        User user2=userRepository.findByUsernameContainingIgnoreCase(username);

       if (user2 == null) {
        throw new RuntimeException("User not found with username: " + username);
}

user2.setBio(user.getBio());
user2.setEmail(user.getEmail());
user2.setPassword(user.getPassword());
user2.setUsername(user.getUsername());
user2.setQualification(user.getQualification());
user2.setRole(user.getRole());
user2.setSubjectExpertise(user.getSubjectExpertise());
user2.setYearsOfExperience(user.getYearsOfExperience());

userRepository.save(user2);
return user2;



    }

    @Override
    public void deleteUser(String username) {
        
        if (userRepository.findByUsernameContainingIgnoreCase(username)==null) {

            throw new RuntimeException("User not found"); 
        }
        userRepository.deleteByUsername(username);
    }

    @Override
public List<User> getUserByRole(String role) {
    List<User> users = userRepository.findByRole(role);

    if (users.isEmpty()) {
        throw new RuntimeException("No users found with role: " + role);
    }

    return users;
}


    @Autowired
    private com.Learn.ELP_backend.service.CourseService courseService;
    
    @Autowired
    private com.Learn.ELP_backend.repository.CourseRepository courseRepository;
    
    @Autowired
    private com.Learn.ELP_backend.repository.VideoRepository videoRepository;

    @Autowired
    private com.Learn.ELP_backend.service.ClassService classService;

    @Override
    public List<User> searchUsers(String keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchUsers'");
    }

    @Override
    public com.Learn.ELP_backend.dto.SystemStatsDTO getSystemStats() {
        long totalUsers = userRepository.count();
        long totalCourses = courseRepository.count();
        long totalVideos = videoRepository.count();
        
        long totalInstructors = userRepository.findByRole("INSTRUCTOR").size();
        long totalStudents = userRepository.findByRole("STUDENT").size();
        
        return com.Learn.ELP_backend.dto.SystemStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalCourses(totalCourses)
                .totalVideos(totalVideos)
                .totalInstructors(totalInstructors)
                .totalStudents(totalStudents)
                .build();
    }

    @Override
    public void deleteCourse(String courseId) {
        courseService.deleteCourse(courseId);
    }

    @Override
    public com.Learn.ELP_backend.model.Course publishCourse(String courseId) {
        return courseService.publishCourse(courseId);
    }

    @Override
    public List<com.Learn.ELP_backend.model.Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @Override
    public List<com.Learn.ELP_backend.model.Course> getCoursesByInstructor(String instructorUsername) {
        User instructor = userRepository.findByUsernameContainingIgnoreCase(instructorUsername);
        if (instructor == null) {
            throw new RuntimeException("Instructor not found with username: " + instructorUsername);
        }
        return courseService.getCoursesByInstructor(instructor.getId());
    }

    @Override
    public List<com.Learn.ELP_backend.model.Class> getAllClasses() {
        return classService.getAllClasses();
    }

    @Override
    public void deleteClass(String classId) {
        classService.deleteClass(classId);
    }
}
