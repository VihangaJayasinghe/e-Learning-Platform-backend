package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.ClassMonth;
import com.Learn.ELP_backend.model.ClassStatus;
import com.Learn.ELP_backend.model.Question;
import com.Learn.ELP_backend.model.Quiz;
import com.Learn.ELP_backend.repository.ClassRepository;
import com.Learn.ELP_backend.service.ClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Override
    public Class createClass(Class classObj) {
        classObj.initializeMonths();
        return classRepository.save(classObj);
    }

    @Override
    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }

    @Override
    public Class getClassById(String id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
    }

    @Override
    public List<Class> getClassesByInstructor(String instructorId) {
        return classRepository.findByInstructorId(instructorId);
    }

    @Override
    public Class updateClass(String id, Class classUpdate) {
        Class existingClass = getClassById(id);

        if (classUpdate.getClassName() != null) {
            existingClass.setClassName(classUpdate.getClassName());
        }
        if (classUpdate.getDescription() != null) {
            existingClass.setDescription(classUpdate.getDescription());
        }
        if (classUpdate.getMonthlyPrice() != null) {
            existingClass.setMonthlyPrice(classUpdate.getMonthlyPrice());
        }

        return classRepository.save(existingClass);
    }

    @Override
    public void deleteClass(String id) {
        Class classObj = getClassById(id);
        classRepository.delete(classObj);
    }

    @Override
    public Class updateClassStatus(String id, ClassStatus status) {
        Class classObj = getClassById(id);
        classObj.setStatus(status);
        return classRepository.save(classObj);
    }

    @Override
    public Class addVideoToMonth(String classId, String yearMonth, String videoId) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);

        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }

        if (!month.getVideoIds().contains(videoId)) {
            month.getVideoIds().add(videoId);
        }

        return classRepository.save(classObj);
    }

    @Override
    public Class removeVideoFromMonth(String classId, String yearMonth, String videoId) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);

        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }

        month.getVideoIds().remove(videoId);
        return classRepository.save(classObj);
    }

    @Override
    public Class releaseMonth(String classId, String yearMonth) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);

        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }

        month.setReleased(true);
        month.setReleaseDate(LocalDateTime.now());

        return classRepository.save(classObj);
    }

    @Override
    public Class unreleaseMonth(String classId, String yearMonth) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);

        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }

        month.setReleased(false);
        month.setReleaseDate(null);

        return classRepository.save(classObj);
    }

    @Override
    public Class extendClassDuration(String classId, int additionalMonths) {
        if (additionalMonths <= 0) {
            throw new RuntimeException("Additional months must be positive");
        }

        Class classObj = getClassById(classId);
        classObj.extendDuration(additionalMonths);

        return classRepository.save(classObj);
    }

    @Override
    public List<String> getMonthVideos(String classId, String yearMonth) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);

        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }

        return month.getVideoIds();
    }

    @Override
    public Quiz addQuizToMonth(String classId, String yearMonth, Quiz quiz) {

        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);

        if (month == null)
            throw new RuntimeException("Month not found");

        quiz.setId(UUID.randomUUID().toString());

        month.getQuizzes().add(quiz);

        classRepository.save(classObj);

        return quiz;
    }

    @Override
    public Quiz updateQuiz(String classId, String yearMonth, String quizId, Quiz quizUpdate) {
        // 1. Get the class
        Class classObj = getClassById(classId);

        // 2. Get the specific month
        ClassMonth month = classObj.findMonth(yearMonth);
        if (month == null) {
            throw new RuntimeException("Month not found: " + yearMonth);
        }

        // 3. Find the quiz inside the month
        Quiz existingQuiz = month.getQuizzes().stream()
                .filter(q -> q.getId().equals(quizId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Quiz not found: " + quizId));

        // 4. Update fields (only non-null ones)
        if (quizUpdate.getQuizTitle() != null) {
            existingQuiz.setQuizTitle(quizUpdate.getQuizTitle());
        }

        if (quizUpdate.getDescription() != null) {
            existingQuiz.setDescription(quizUpdate.getDescription());
        }

        if (quizUpdate.getQuestions() != null) {
            existingQuiz.setQuestions(quizUpdate.getQuestions());
        }

        // 5. Save class back to DB
        classRepository.save(classObj);

        return existingQuiz;

    }

    @Override
    public void deleteQuiz(String classId, String yearMonth, String quizId) {
        // 1. Get the class
        Class classObj = getClassById(classId);

        // 2. Get the month
        ClassMonth month = classObj.findMonth(yearMonth);
        if (month == null) {
            throw new RuntimeException("Month not found: " + yearMonth);
        }

        // 3. Remove quiz by ID
        boolean removed = month.getQuizzes().removeIf(q -> q.getId().equals(quizId));

        if (!removed) {
            throw new RuntimeException("Quiz not found: " + quizId);
        }

        // 4. Save
        classRepository.save(classObj);
    }

    @Override
    public Question addQuestionToQuiz(String classId, String yearMonth, String quizId, Question question) {
        Class classModel = findClass(classId);

        ClassMonth month = findMonth(classModel, yearMonth);

        Quiz quiz = findQuiz(month, quizId);

        // Assign a new ID for the question
        question.setQuestionId(UUID.randomUUID().toString());

        quiz.getQuestions().add(question);

        classRepository.save(classModel);

        return question;
    }

    @Override
    public Question updateQuestionInQuiz(String classId, String yearMonth, String quizId, String questionId,
            Question questionUpdate) {
        Class classModel = findClass(classId);
        ClassMonth month = findMonth(classModel, yearMonth);
        Quiz quiz = findQuiz(month, quizId);

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

        classRepository.save(classModel);
        return existing;
    }

    @Override
    public void deleteQuestionFromQuiz(String classId, String yearMonth, String quizId, String questionId) {
        Class classModel = findClass(classId);
        ClassMonth month = findMonth(classModel, yearMonth);
        Quiz quiz = findQuiz(month, quizId);

        boolean removed = quiz.getQuestions()
                .removeIf(q -> questionId.equals(q.getQuestionId()));

        if (!removed) {
            throw new RuntimeException("Question not found: " + questionId);
        }

        classRepository.save(classModel);
    }

    private Class findClass(String classId) {
        return classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found: " + classId));
    }

    private ClassMonth findMonth(Class classModel, String yearMonth) {
        return classModel.getMonths().stream()
                .filter(m -> m.getYearMonth().equals(yearMonth))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Month not found: " + yearMonth));
    }

    private Quiz findQuiz(ClassMonth month, String quizId) {
        return month.getQuizzes().stream()
                .filter(q -> q.getId().equals(quizId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Quiz not found: " + quizId));
    }

}