package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    List<Quiz> findByClassId(String classId);
    List<Quiz> findByClassIdAndMonthId(String classId, String monthId);
}

