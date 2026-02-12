package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.*;
import com.Learn.ELP_backend.repository.QuizAttemptRepository;
import com.Learn.ELP_backend.repository.QuizRepository;
import com.Learn.ELP_backend.repository.QuizResultRepository;
import com.Learn.ELP_backend.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizAttemptServiceImpl implements QuizAttemptService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private QuizResultRepository quizResultRepository;
    
    @Override
    public QuizAttempt startQuizAttempt(String quizId, String studentId, String studentName) {
        // Get quiz
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found: " + quizId));

        // Check if student has already attempted this quiz
        List<QuizAttempt> existingAttempts = quizAttemptRepository.findByQuizIdAndStudentId(quizId, studentId);
        if (!existingAttempts.isEmpty()) {
            // For now, allow multiple attempts. Hoping to change it later based on the requirement........
        }

        // Create quiz attempt with questions (without answers)
        QuizAttempt attempt = QuizAttempt.builder()
                .quizId(quizId)
                .studentId(studentId)
                .studentName(studentName)
                .classId(quiz.getClassId())
                .quizTitle(quiz.getQuizTitle())
                .totalQuestions(quiz.getQuestions() != null ? quiz.getQuestions().size() : 0)
                .isCompleted(false)
                .build();

        // Initialize question attempts (empty the answers)
        if (quiz.getQuestions() != null) {
            List<QuizAttempt.QuestionAttempt> questionAttempts = quiz.getQuestions().stream()
                    .map(q -> QuizAttempt.QuestionAttempt.builder()
                            .questionId(q.getQuestionId())
                            .questionText(q.getQuestionText())
                            .options(q.getOptions())
                            .selectedAnswerIndex(-1)
                            .correctAnswerIndex(q.getCorrectAnswerIndex())
                            .isCorrect(false)
                            .build())
                    .collect(Collectors.toList());
            attempt.setQuestionAttempts(questionAttempts);
        }

        return quizAttemptRepository.save(attempt);
    }

     @Override
    public QuizAttempt submitAnswer(String attemptId, String questionId, int selectedAnswerIndex) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found: " + attemptId));
        
        if (attempt.isCompleted()) {
            throw new RuntimeException("Cannot submit answer: Quiz already completed");
        }
        
        // Find the question attempt
        QuizAttempt.QuestionAttempt questionAttempt = attempt.getQuestionAttempts().stream()
                .filter(qa -> qa.getQuestionId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question not found in this attempt: " + questionId));
        
        // Update the answer
        questionAttempt.setSelectedAnswerIndex(selectedAnswerIndex);
        
        // Check if answer is correct
        questionAttempt.setCorrect(selectedAnswerIndex == questionAttempt.getCorrectAnswerIndex());
        
        return quizAttemptRepository.save(attempt);
    }

    @Override
    public QuizAttempt completeQuizAttempt(String attemptId) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found: " + attemptId));
        
        if (attempt.isCompleted()) {
            throw new RuntimeException("Quiz already completed");
        }
        
        // Calculate score
        int correctAnswers = 0;
        for (QuizAttempt.QuestionAttempt qa : attempt.getQuestionAttempts()) {
            if (qa.isCorrect()) {
                correctAnswers++;
            }
        }
        
        attempt.setCorrectAnswers(correctAnswers);
        attempt.setScore(attempt.getTotalQuestions() > 0 ? 
                (double) correctAnswers / attempt.getTotalQuestions() * 100 : 0);
        attempt.setCompleted(true);
        attempt.setCompletedAt(LocalDateTime.now());
        
        // Save the attempt
        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);
        
        // Create/update quiz result (optional)
        createOrUpdateQuizResult(savedAttempt);
        
        return savedAttempt;
    }

    private void createOrUpdateQuizResult(QuizAttempt attempt) {
        // Check if result already exists
        QuizResult existingResult = quizResultRepository.findByQuizIdAndStudentId(
                attempt.getQuizId(), attempt.getStudentId());
        
        if (existingResult == null) {
            // Create new result
            QuizResult result = QuizResult.builder()
                    .quizId(attempt.getQuizId())
                    .quizTitle(attempt.getQuizTitle())
                    .studentId(attempt.getStudentId())
                    .studentName(attempt.getStudentName())
                    .classId(attempt.getClassId())
                    .totalQuestions(attempt.getTotalQuestions())
                    .correctAnswers(attempt.getCorrectAnswers())
                    .scorePercentage(attempt.getScore())
                    .attemptDate(attempt.getAttemptDate())
                    .build();
            quizResultRepository.save(result);
        } else {
            // Update existing result (keep only the best attempt)
            if (attempt.getScore() > existingResult.getScorePercentage()) {
                existingResult.setCorrectAnswers(attempt.getCorrectAnswers());
                existingResult.setScorePercentage(attempt.getScore());
                existingResult.setAttemptDate(attempt.getAttemptDate());
                quizResultRepository.save(existingResult);
            }
        }
    }

     @Override
    public QuizAttempt getQuizAttemptById(String attemptId) {
        return quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found: " + attemptId));
    }
    
    @Override
    public List<QuizAttempt> getStudentAttempts(String studentId) {
        return quizAttemptRepository.findByStudentId(studentId);
    }
    
    @Override
    public List<QuizAttempt> getQuizAttempts(String quizId) {
        return quizAttemptRepository.findByQuizId(quizId);
    }
    
    @Override
    public List<QuizAttempt> getStudentQuizAttempts(String quizId, String studentId) {
        return quizAttemptRepository.findByQuizIdAndStudentId(quizId, studentId);
    }

     @Override
    public List<QuizResult> getQuizResults(String quizId) {
        List<QuizResult> results = quizResultRepository.findByQuizId(quizId);
        
        // Sort by score (descending)
        results.sort((a, b) -> Double.compare(b.getScorePercentage(), a.getScorePercentage()));
        
        // Add ranks
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setRankInClass(i + 1);
        }
        
        // Calculate class average
        if (!results.isEmpty()) {
            double average = results.stream()
                    .mapToDouble(QuizResult::getScorePercentage)
                    .average()
                    .orElse(0);
            
            results.forEach(r -> r.setClassAverageScore(average));
        }
        
        return results;
    }

     @Override
    public QuizResult getStudentQuizResult(String quizId, String studentId) {
        return quizResultRepository.findByQuizIdAndStudentId(quizId, studentId);
    }
    
    @Override
    public Map<String, Object> getQuizStatistics(String quizId) {
        List<QuizResult> results = quizResultRepository.findByQuizId(quizId);
        
        Map<String, Object> stats = new HashMap<>();
        
        if (results.isEmpty()) {
            stats.put("totalAttempts", 0);
            stats.put("averageScore", 0);
            stats.put("highestScore", 0);
            stats.put("lowestScore", 0);
            return stats;
        }
        
        double[] scores = results.stream()
                .mapToDouble(QuizResult::getScorePercentage)
                .toArray();
        
        stats.put("totalAttempts", results.size());
        stats.put("averageScore", Arrays.stream(scores).average().orElse(0));
        stats.put("highestScore", Arrays.stream(scores).max().orElse(0));
        stats.put("lowestScore", Arrays.stream(scores).min().orElse(0));
        
        // Score distribution
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("A (90-100%)", results.stream().filter(r -> r.getScorePercentage() >= 90).count());
        distribution.put("B (80-89%)", results.stream().filter(r -> r.getScorePercentage() >= 80 && r.getScorePercentage() < 90).count());
        distribution.put("C (70-79%)", results.stream().filter(r -> r.getScorePercentage() >= 70 && r.getScorePercentage() < 80).count());
        distribution.put("D (60-69%)", results.stream().filter(r -> r.getScorePercentage() >= 60 && r.getScorePercentage() < 70).count());
        distribution.put("F (0-59%)", results.stream().filter(r -> r.getScorePercentage() < 60).count());
        
        stats.put("scoreDistribution", distribution);
        
        return stats;
    }
    
    @Override
    public void deleteQuizAttempt(String attemptId) {
        quizAttemptRepository.deleteById(attemptId);
    }
}






