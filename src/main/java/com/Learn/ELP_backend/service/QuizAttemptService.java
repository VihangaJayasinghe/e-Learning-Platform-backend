package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.QuizAttempt;
import com.Learn.ELP_backend.model.QuizResult;
import java.util.List;
import java.util.Map;

public interface QuizAttemptService {
    // Start a new quiz attempt
    QuizAttempt startQuizAttempt(String quizId, String studentId, String studentName);
    
    // Submit answer for a question
    QuizAttempt submitAnswer(String attemptId, String questionId, int selectedAnswerIndex);
    
    // Complete the quiz attempt and calculate score
    QuizAttempt completeQuizAttempt(String attemptId);
    
    // Get quiz attempt by ID
    QuizAttempt getQuizAttemptById(String attemptId);
    
    // Get all attempts for a student
    List<QuizAttempt> getStudentAttempts(String studentId);
    
    // Get all attempts for a quiz
    List<QuizAttempt> getQuizAttempts(String quizId);
    
    // Get student's attempts for a specific quiz
    List<QuizAttempt> getStudentQuizAttempts(String quizId, String studentId);
    
    // Get results/leaderboard for a quiz
    List<QuizResult> getQuizResults(String quizId);
    
    // Get student's result for a quiz
    QuizResult getStudentQuizResult(String quizId, String studentId);
    
    // Get class statistics for a quiz
    Map<String, Object> getQuizStatistics(String quizId);
    
    // Delete an attempt (for cleanup)
    void deleteQuizAttempt(String attemptId);
}