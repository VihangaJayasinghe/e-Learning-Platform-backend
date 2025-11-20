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
user2.setDepartment(user.getDepartment());
user2.setEmail(user.getEmail());
user2.setNic(user.getNic());
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


    @Override
    public List<User> searchUsers(String keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchUsers'");
    }

}
