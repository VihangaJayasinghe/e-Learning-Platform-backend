package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Quiz;
import com.Learn.ELP_backend.repository.QuizRepository;
import com.Learn.ELP_backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(String quizId, Quiz quizUpdate) {
        Quiz existingQuiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found: " + quizId));

        if (quizUpdate.getQuizTitle() != null) existingQuiz.setQuizTitle(quizUpdate.getQuizTitle());
        if (quizUpdate.getDescription() != null) existingQuiz.setDescription(quizUpdate.getDescription());
        if (quizUpdate.getQuestions() != null) existingQuiz.setQuestions(quizUpdate.getQuestions());

        return quizRepository.save(existingQuiz);
    }

    @Override
    public void deleteQuiz(String quizId) {
        quizRepository.deleteById(quizId);
    }

    @Override
    public Quiz getQuizById(String quizId) {
        return quizRepository.findById(quizId).orElse(null);
    }

    @Override
    public List<Quiz> getQuizzesByClass(String classId) {
        return quizRepository.findByClassId(classId);
    }

    @Override
    public List<Quiz> getQuizzesByClassAndMonth(String classId, String yearMonth) {
        return quizRepository.findByClassIdAndYearMonth(classId, yearMonth);
    }
}
