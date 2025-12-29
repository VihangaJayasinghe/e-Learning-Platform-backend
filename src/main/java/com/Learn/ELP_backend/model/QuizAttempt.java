package com.Learn.ELP_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "quiz_attempts")
public class QuizAttempt {
    @Id
    private String id;
    
    private String quizId;
    private String studentId;
    private String studentName;
    private String classId;
    private String quizTitle;
    
    @Builder.Default
    private List<QuestionAttempt> questionAttempts = new ArrayList<>();
    
    private int totalQuestions;
    private int correctAnswers;
    private double score;

    @Builder.Default
    private LocalDateTime attemptDate = LocalDateTime.now();
    
    private LocalDateTime completedAt;
    private boolean isCompleted;
    private Integer timeTakenSeconds;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionAttempt {
        private String questionId;
        private String questionText;
        private List<String> options;
        private int selectedAnswerIndex;
        private int correctAnswerIndex;
        private boolean isCorrect;
    }
}