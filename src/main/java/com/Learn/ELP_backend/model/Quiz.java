package com.Learn.ELP_backend.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@lombok.Builder
@Document(collection = "quizes")
public class Quiz {
    @Id
    private String Id;

    private String quizTitle;
    private String description;
    private List<Question> questions;
    // timeLimit
    private String createdBy;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
