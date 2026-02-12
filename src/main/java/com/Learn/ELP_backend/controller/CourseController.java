package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Course;
import com.Learn.ELP_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.createCourse(course));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable String id, @RequestBody Course courseUpdate) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseUpdate));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }
    
    @GetMapping("/published/{id}")
    public ResponseEntity<Course> getPublishedCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getPublishedCourseById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    @GetMapping("/published")
    public ResponseEntity<List<Course>> getPublishedCourses() {
        return ResponseEntity.ok(courseService.getPublishedCourses());
    }
    
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable String instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Boolean isFree) {
        return ResponseEntity.ok(courseService.searchCourses(keyword, subject, topic, level, isFree));
    }
    
    @PostMapping("/{id}/publish")
    public ResponseEntity<Course> publishCourse(@PathVariable String id) {
        return ResponseEntity.ok(courseService.publishCourse(id));
    }
    
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<Course> unpublishCourse(@PathVariable String id) {
        return ResponseEntity.ok(courseService.unpublishCourse(id));
    }
    
    @PostMapping("/{courseId}/videos/{videoId}")
    public ResponseEntity<Course> addVideoToCourse(
            @PathVariable String courseId,
            @PathVariable String videoId) {
        return ResponseEntity.ok(courseService.addVideoToCourse(courseId, videoId));
    }
    
    @DeleteMapping("/{courseId}/videos/{videoId}")
    public ResponseEntity<Course> removeVideoFromCourse(
            @PathVariable String courseId,
            @PathVariable String videoId) {
        return ResponseEntity.ok(courseService.removeVideoFromCourse(courseId, videoId));
    }
    
    @PostMapping("/{courseId}/documents/{documentId}")
    public ResponseEntity<Course> addDocumentToCourse(
            @PathVariable String courseId,
            @PathVariable String documentId) {
        return ResponseEntity.ok(courseService.addDocumentToCourse(courseId, documentId));
    }
    
    @DeleteMapping("/{courseId}/documents/{documentId}")
    public ResponseEntity<Course> removeDocumentFromCourse(
            @PathVariable String courseId,
            @PathVariable String documentId) {
        return ResponseEntity.ok(courseService.removeDocumentFromCourse(courseId, documentId));
    }
    
    @PostMapping("/{courseId}/quizzes/{quizId}")
    public ResponseEntity<Course> addQuizToCourse(
            @PathVariable String courseId,
            @PathVariable String quizId) {
        return ResponseEntity.ok(courseService.addQuizToCourse(courseId, quizId));
    }
    
    @DeleteMapping("/{courseId}/quizzes/{quizId}")
    public ResponseEntity<Course> removeQuizFromCourse(
            @PathVariable String courseId,
            @PathVariable String quizId) {
        return ResponseEntity.ok(courseService.removeQuizFromCourse(courseId, quizId));
    }
}