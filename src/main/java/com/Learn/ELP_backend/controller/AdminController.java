package com.Learn.ELP_backend.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.service.AdminSecrity;
import com.Learn.ELP_backend.service.AdminService;
@RestController
@RequestMapping("/api/admins")

public class AdminController {


    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminSecrity adminSecrity;

 @GetMapping("/users")
    public ResponseEntity<?> getAll() {
        try {
            List<User> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving users: " + e.getMessage());
        }
    }
   
@GetMapping("/users/{username}")
public ResponseEntity<?> getuserbyname(@PathVariable String username) {
    try {
        User user = adminService.getuserByName(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User not found with username: " + username);
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Error retrieving user: " + e.getMessage());
    }
}





@GetMapping("/users/role/{role}")
    public ResponseEntity<?> getByRole(@PathVariable String role) {
        try {
            List<User> users = adminService.getUserByRole(role);
            if (users != null && !users.isEmpty()) {
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                        .body("No users found with role: " + role);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving users by role: " + e.getMessage());
        }
    }


@PutMapping("/users/{username}/{key}")
public ResponseEntity<?> updateUser(
        @PathVariable String username,
        @PathVariable String key,
        @RequestBody User request) {
    try {
        // Validate security key first
        adminSecrity.validateKey(key);
        
        // If key is valid, proceed with update
        User updatedUser = adminService.updateUser(username, request);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User not found with username: " + username);
        }
    } catch (SecurityException e) {
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                .body("Invalid security key: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Error updating user: " + e.getMessage());
    }
}


    @DeleteMapping("/users/{username}/{key}")
public ResponseEntity<?> delete(
        @PathVariable String username, 
        @PathVariable String key) {
    try {
        // Validate security key first
        adminSecrity.validateKey(key);
        
        // If key is valid, proceed with deletion
        adminService.deleteUser(username);
        return ResponseEntity.ok("User '" + username + "' deleted successfully");
    } catch (SecurityException e) {
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                .body("Invalid security key: " + e.getMessage());
    } catch (RuntimeException e) {
        if (e.getMessage().contains("not found")) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User not found with username: " + username);
        }
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Error deleting user: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Error deleting user: " + e.getMessage());
    }
}


}
