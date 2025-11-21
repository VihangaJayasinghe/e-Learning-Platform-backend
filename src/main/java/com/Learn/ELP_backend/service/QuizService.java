package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Quiz;

import java.util.List;

public interface QuizService {
    Quiz createQuiz(Quiz quiz);
    Quiz updateQuiz(String quizId, Quiz quizUpdate);
    void deleteQuiz(String quizId);
    Quiz getQuizById(String quizId);
    List<Quiz> getQuizzesByClass(String classId);
    List<Quiz> getQuizzesByClassAndMonth(String classId, String yearMonth);
}

