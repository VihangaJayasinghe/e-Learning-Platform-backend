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



}
