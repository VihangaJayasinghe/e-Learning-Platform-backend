package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);
    User findByEmail(String email);
    User findByPasswordResetToken(String token);

}
