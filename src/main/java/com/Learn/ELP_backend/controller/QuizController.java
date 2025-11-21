package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Quiz;
import com.Learn.ELP_backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable String quizId, @RequestBody Quiz quizUpdate) {
        return ResponseEntity.ok(quizService.updateQuiz(quizId, quizUpdate));
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable String quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable String quizId) {
        return ResponseEntity.ok(quizService.getQuizById(quizId));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Quiz>> getQuizzesByClass(@PathVariable String classId) {
        return ResponseEntity.ok(quizService.getQuizzesByClass(classId));
    }

    @GetMapping("/class/{classId}/month/{yearMonth}")
    public ResponseEntity<List<Quiz>> getQuizzesByClassAndMonth(@PathVariable String classId,
                                                                @PathVariable String yearMonth) {
        return ResponseEntity.ok(quizService.getQuizzesByClassAndMonth(classId, yearMonth));
    }
}
