package com.Learn.ELP_backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.Learn.ELP_backend.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsernameContainingIgnoreCase(String username);
    User deleteByUsername(String username);
    List<User> findByRole(String role);

    User findByUsername(String username);
    User findByEmail(String email);
    User findByPasswordResetToken(String token);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
