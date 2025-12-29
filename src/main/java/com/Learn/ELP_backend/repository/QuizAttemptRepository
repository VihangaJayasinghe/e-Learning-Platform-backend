package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.QuizAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizAttemptRepository extends MongoRepository<QuizAttempt, String> {
    List<QuizAttempt> findByQuizId(String quizId);
    List<QuizAttempt> findByStudentId(String studentId);
    List<QuizAttempt> findByClassId(String classId);
    List<QuizAttempt> findByQuizIdAndStudentId(String quizId, String studentId);
    List<QuizAttempt> findByQuizIdAndClassId(String quizId, String classId);
}