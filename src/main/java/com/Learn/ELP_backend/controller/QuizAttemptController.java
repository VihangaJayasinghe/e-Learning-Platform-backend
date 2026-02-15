package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.QuizAttempt;
import com.Learn.ELP_backend.model.QuizResult;
import com.Learn.ELP_backend.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz-attempts")
public class QuizAttemptController {

    @Autowired
    private QuizAttemptService quizAttemptService;
    
    // Start a new quiz attempt
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @PostMapping("/start")
    public ResponseEntity<QuizAttempt> startQuizAttempt(
            @RequestParam String quizId,
            @RequestParam String studentId,
            @RequestParam String studentName) {
        QuizAttempt attempt = quizAttemptService.startQuizAttempt(quizId, studentId, studentName);
        return ResponseEntity.ok(attempt);
    }
    
    // Submit an answer
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @PostMapping("/{attemptId}/answer")
    public ResponseEntity<QuizAttempt> submitAnswer(
            @PathVariable String attemptId,
            @RequestParam String questionId,
            @RequestParam int selectedAnswerIndex) {
        QuizAttempt attempt = quizAttemptService.submitAnswer(attemptId, questionId, selectedAnswerIndex);
        return ResponseEntity.ok(attempt);
    }
    
    // Complete the quiz and get results
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @PostMapping("/{attemptId}/complete")
    public ResponseEntity<QuizAttempt> completeQuizAttempt(@PathVariable String attemptId) {
        QuizAttempt attempt = quizAttemptService.completeQuizAttempt(attemptId);
        return ResponseEntity.ok(attempt);
    }
    
    // Get specific attempt
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/{attemptId}")
    public ResponseEntity<QuizAttempt> getQuizAttempt(@PathVariable String attemptId) {
        QuizAttempt attempt = quizAttemptService.getQuizAttemptById(attemptId);
        return ResponseEntity.ok(attempt);
    }
    
    // Get all attempts for a student
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<QuizAttempt>> getStudentAttempts(@PathVariable String studentId) {
        List<QuizAttempt> attempts = quizAttemptService.getStudentAttempts(studentId);
        return ResponseEntity.ok(attempts);
    }
    
    // Get attempts for a specific quiz
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizAttempt>> getQuizAttempts(@PathVariable String quizId) {
        List<QuizAttempt> attempts = quizAttemptService.getQuizAttempts(quizId);
        return ResponseEntity.ok(attempts);
    }
    
    // Get a student's attempts for a specific quiz
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/quiz/{quizId}/student/{studentId}")
    public ResponseEntity<List<QuizAttempt>> getStudentQuizAttempts(
            @PathVariable String quizId,
            @PathVariable String studentId) {
        List<QuizAttempt> attempts = quizAttemptService.getStudentQuizAttempts(quizId, studentId);
        return ResponseEntity.ok(attempts);
    }
    
    // Get quiz results/leaderboard
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @GetMapping("/quiz/{quizId}/results")
    public ResponseEntity<List<QuizResult>> getQuizResults(@PathVariable String quizId) {
        List<QuizResult> results = quizAttemptService.getQuizResults(quizId);
        return ResponseEntity.ok(results);
    }
    
    // Get a student's result for a quiz
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/quiz/{quizId}/student/{studentId}/result")
    public ResponseEntity<QuizResult> getStudentQuizResult(
            @PathVariable String quizId,
            @PathVariable String studentId) {
        QuizResult result = quizAttemptService.getStudentQuizResult(quizId, studentId);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Get quiz statistics (for teachers)
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @GetMapping("/quiz/{quizId}/statistics")
    public ResponseEntity<Map<String, Object>> getQuizStatistics(@PathVariable String quizId) {
        Map<String, Object> stats = quizAttemptService.getQuizStatistics(quizId);
        return ResponseEntity.ok(stats);
    }
    
    // Delete an attempt
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{attemptId}")
    public ResponseEntity<Void> deleteQuizAttempt(@PathVariable String attemptId) {
        quizAttemptService.deleteQuizAttempt(attemptId);
        return ResponseEntity.ok().build();
    }
}