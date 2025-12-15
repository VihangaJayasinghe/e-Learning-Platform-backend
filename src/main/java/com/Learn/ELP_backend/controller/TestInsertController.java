package com.Learn.ELP_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/test")

public class TestInsertController {

@Autowired
private UserRepository userRepository;
    @PostMapping("/add-user")

    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

@GetMapping("/users")
public List<User> getall(){
    return userRepository.findAll();
}
}
   



