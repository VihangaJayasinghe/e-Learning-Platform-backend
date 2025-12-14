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
@Document(collection = "quizzes")
public class Quiz {
    @Id
    private String id;

    private String quizTitle;
    private String description;
    private List<Question> questions;
    private String createdBy;
    private String classId;
    private String monthId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String className;
    private String monthDisplayName;

}
