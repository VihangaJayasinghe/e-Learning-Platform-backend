package com.Learn.ELP_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "quiz_results")
public class QuizResult {
    @Id
    private String id;
    
    private String quizId;
    private String quizTitle;
    private String studentId;
    private String studentName;
    private String classId;
    
    private int totalQuestions;
    private int correctAnswers;
    private double scorePercentage;
    
    @Builder.Default
    private LocalDateTime attemptDate = LocalDateTime.now();
    
    // For ranking/statistics
    private Integer rankInClass;
    private Double classAverageScore;
}