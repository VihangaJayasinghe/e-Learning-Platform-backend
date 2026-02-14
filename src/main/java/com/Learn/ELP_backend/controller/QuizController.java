package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Question;
import com.Learn.ELP_backend.model.Quiz;
import com.Learn.ELP_backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PutMapping("/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable String quizId, @RequestBody Quiz quizUpdate) {
        return ResponseEntity.ok(quizService.updateQuiz(quizId, quizUpdate));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable String quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable String quizId) {
        return ResponseEntity.ok(quizService.getQuizById(quizId));
    }

    // Get quiz for student (without correct answers)
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/{quizId}/attempt")
    public ResponseEntity<Quiz> getQuizForAttempt(@PathVariable String quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(question -> {
                question.setCorrectAnswerIndex(-1); // Hide correct answer
            });
        }
    
        return ResponseEntity.ok(quiz);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Quiz>> getQuizzesByClass(@PathVariable String classId) {
        return ResponseEntity.ok(quizService.getQuizzesByClass(classId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/class/{classId}/month/{monthId}")
    public ResponseEntity<List<Quiz>> getQuizzesByClassAndMonth(@PathVariable String classId,
                                                                @PathVariable String monthId) {
        return ResponseEntity.ok(quizService.getQuizzesByClassAndMonth(classId, monthId));
    }

    //Question Management
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/{quizId}/add-question")
    public ResponseEntity<Quiz> addQuestionToQuiz(@PathVariable String quizId, 
                                                  @RequestBody Question question) {
        return ResponseEntity.ok(quizService.addQuestionToQuiz(quizId, question));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PutMapping("/{quizId}/update-question/{questionId}")
    public ResponseEntity<Quiz> updateQuestionInQuiz(@PathVariable String quizId,
                                                     @PathVariable String questionId,
                                                     @RequestBody Question questionUpdate) {
        return ResponseEntity.ok(quizService.updateQuestionInQuiz(quizId, questionId, questionUpdate));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @DeleteMapping("/{quizId}/delete-question/{questionId}")
    public ResponseEntity<Quiz> deleteQuestionFromQuiz(@PathVariable String quizId,
                                                       @PathVariable String questionId) {
        return ResponseEntity.ok(quizService.deleteQuestionFromQuiz(quizId, questionId));
    }


}
