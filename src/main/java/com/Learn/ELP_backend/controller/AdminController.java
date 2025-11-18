package com.Learn.ELP_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.service.AdminService;
@RestController
@RequestMapping("/api/admins")

public class AdminController {


    @Autowired
    private AdminService adminService;


@GetMapping("/users")
public List<User> getall(){
    return adminService.getAllUsers();
}
   
 @GetMapping("/users/{username}")
    public User getuserbyname(@PathVariable String username) {
        return adminService.getuserByName(username);
    }
@GetMapping("/users/role/{role}")
    public List<User> getByRole(@PathVariable String role) {
        return adminService.getUserByRole(role);
    }
@PutMapping("/users/{username}")
    public User updateUser(
            @PathVariable String username,
            @RequestBody User request
    ) {
        return adminService.updateUser(username, request);
    }

}
