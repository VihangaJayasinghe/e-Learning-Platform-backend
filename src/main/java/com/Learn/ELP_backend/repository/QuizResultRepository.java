package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.QuizResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizResultRepository extends MongoRepository<QuizResult, String> {
    List<QuizResult> findByQuizId(String quizId);
    List<QuizResult> findByStudentId(String studentId);
    List<QuizResult> findByClassId(String classId);
    QuizResult findByQuizIdAndStudentId(String quizId, String studentId);
}