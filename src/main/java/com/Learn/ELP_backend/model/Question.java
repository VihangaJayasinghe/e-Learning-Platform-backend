package com.Learn.ELP_backend.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private String questionId;
    private String questionText;
    private List<String> options;
    private int correctAnswerIndex;
}
