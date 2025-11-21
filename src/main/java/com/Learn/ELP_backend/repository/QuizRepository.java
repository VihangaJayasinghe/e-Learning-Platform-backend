package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    @Query("{ 'classId': ?0 }")
List<Quiz> findByClassId(String classId);

@Query("{ 'classId': ?0, 'yearMonth': ?1 }")
List<Quiz> findByClassIdAndYearMonth(String classId, String yearMonth);
}

