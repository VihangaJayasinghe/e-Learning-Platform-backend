package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.ClassMonth;
import com.Learn.ELP_backend.model.Question;
import com.Learn.ELP_backend.model.Quiz;
import com.Learn.ELP_backend.repository.ClassRepository;
import com.Learn.ELP_backend.repository.QuizRepository;
import com.Learn.ELP_backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public Quiz createQuiz(Quiz quiz) {
        // Validate class exists
        Class classObj = classRepository.findById(quiz.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found: " + quiz.getClassId()));

        // Set additional info
        String quizId = UUID.randomUUID().toString();
        quiz.setId(quizId);
        quiz.setClassName(classObj.getClassName());

        // Validate month exists
        ClassMonth month = classObj.getMonths().stream()
                .filter(m -> m.getYearMonth().equals(quiz.getMonthId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Month not found in class"));

        // Set additional info
        quiz.setClassName(classObj.getClassName());
        quiz.setMonthDisplayName(month.getDisplayName());

        // Save quiz first
        Quiz savedQuiz = quizRepository.save(quiz);

        // Add quiz ID to ClassMonth
        if (!month.getQuizIds().contains(quizId)) {
            month.getQuizIds().add(quizId);
            classRepository.save(classObj);
        }

        return savedQuiz;
    }

    @Override
    public Quiz getQuizById(String id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found: " + id));
    }

    @Override
    public List<Quiz> getQuizzesByClass(String classId) {
        return quizRepository.findByClassId(classId);
    }

    @Override
    public List<Quiz> getQuizzesByClassAndMonth(String classId, String monthId) {
        return quizRepository.findByClassIdAndMonthId(classId, monthId);
    }

    @Override
    public Quiz updateQuiz(String id, Quiz quizUpdate) {
        Quiz existingQuiz = getQuizById(id);

        if (quizUpdate.getQuizTitle() != null) {
            existingQuiz.setQuizTitle(quizUpdate.getQuizTitle());
        }
        if (quizUpdate.getDescription() != null) {
            existingQuiz.setDescription(quizUpdate.getDescription());
        }
        if (quizUpdate.getQuestions() != null) {
            existingQuiz.setQuestions(quizUpdate.getQuestions());
        }

        return quizRepository.save(existingQuiz);
    }

    @Override
    public void deleteQuiz(String id) {
        Quiz quiz = getQuizById(id);

        // Remove from ClassMonth first
        Class classObj = classRepository.findById(quiz.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        ClassMonth month = classObj.getMonths().stream()
                .filter(m -> m.getYearMonth().equals(quiz.getMonthId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ClassMonth not found for month: " + quiz.getMonthId()));

        month.getQuizIds().remove(id);
        classRepository.save(classObj);

        // Delete quiz
        quizRepository.delete(quiz);
    }

    @Override
    public Quiz addQuestionToQuiz(String quizId, Question question) {
        Quiz quiz = getQuizById(quizId);

        question.setQuestionId(UUID.randomUUID().toString());
        quiz.getQuestions().add(question);

        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuestionInQuiz(String quizId, String questionId, Question questionUpdate) {
        Quiz quiz = getQuizById(quizId);

        Question existing = quiz.getQuestions().stream()
                .filter(q -> questionId.equals(q.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (questionUpdate.getQuestionText() != null) {
            existing.setQuestionText(questionUpdate.getQuestionText());
        }
        if (questionUpdate.getOptions() != null) {
            existing.setOptions(questionUpdate.getOptions());
        }
        if (questionUpdate.getCorrectAnswerIndex() != null) {
            existing.setCorrectAnswerIndex(questionUpdate.getCorrectAnswerIndex());
        }

        return quizRepository.save(quiz);
    }

    @Override
    public Quiz deleteQuestionFromQuiz(String quizId, String questionId) {
        Quiz quiz = getQuizById(quizId);

        boolean removed = quiz.getQuestions()
                .removeIf(q -> questionId.equals(q.getQuestionId()));

        if (!removed) {
            throw new RuntimeException("Question not found: " + questionId);
        }

        return quizRepository.save(quiz);
    }
}