package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Question;
import com.Learn.ELP_backend.model.Quiz;


import java.util.List;

public interface QuizService {
    Quiz createQuiz(Quiz quiz);
    Quiz updateQuiz(String quizId, Quiz quizUpdate);
    void deleteQuiz(String quizId);
    Quiz getQuizById(String quizId);
    List<Quiz> getQuizzesByClass(String classId);
    List<Quiz> getQuizzesByClassAndMonth(String classId, String monthId);
    // Question management
    Quiz addQuestionToQuiz(String quizId, Question question);
    Quiz updateQuestionInQuiz(String quizId, String questionId, Question questionUpdate);
    Quiz deleteQuestionFromQuiz(String quizId, String questionId);
}
