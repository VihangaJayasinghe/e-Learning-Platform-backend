package com.Learn.ELP_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Learn.ELP_backend.model.Quiz;
import com.Learn.ELP_backend.service.ClassService;

@RestController
@RequestMapping("api/classes/{classId}/months/{yearMonth}/quizzes")
public class QuizController {

    @Autowired  
    private ClassService classService;

    @PostMapping
    public ResponseEntity<Quiz> addQuiz(
            @PathVariable String classId,
            @PathVariable String yearMonth,
            @RequestBody Quiz quiz) {
        
        Quiz created = classService.addQuizToMonth(classId, yearMonth, quiz);
            return ResponseEntity.ok(created);
    }
    

    @PutMapping("/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(
        @PathVariable String classId,
        @PathVariable String yearMonth,
        @PathVariable String quizId,
        @RequestBody Quiz quizUpdate) {

    Quiz updated = classService.updateQuiz(classId, yearMonth, quizId, quizUpdate);
    return ResponseEntity.ok(updated);
}


    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(
        @PathVariable String classId,
        @PathVariable String yearMonth,
        @PathVariable String quizId) {

    classService.deleteQuiz(classId, yearMonth, quizId);
    return ResponseEntity.ok().build();
}

}
