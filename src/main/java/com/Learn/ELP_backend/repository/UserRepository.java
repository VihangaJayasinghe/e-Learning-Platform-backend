package com.Learn.ELP_backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Learn.ELP_backend.model.User;
import com.google.common.base.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    User findByUsernameContainingIgnoreCase(String username);
    User deleteByUsername(String username);
    List<User> findByRole(String role);

   
}
