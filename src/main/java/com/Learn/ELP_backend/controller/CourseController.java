package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Course;
import com.Learn.ELP_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.createCourse(course));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable String id, @RequestBody Course courseUpdate) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseUpdate));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/published/{id}")
    public ResponseEntity<Course> getPublishedCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getPublishedCourseById(id));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/published")
    public ResponseEntity<List<Course>> getPublishedCourses() {
        return ResponseEntity.ok(courseService.getPublishedCourses());
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable String instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Boolean isFree) {
        return ResponseEntity.ok(courseService.searchCourses(keyword, subject, topic, level, isFree));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/{id}/publish")
    public ResponseEntity<Course> publishCourse(@PathVariable String id) {
        return ResponseEntity.ok(courseService.publishCourse(id));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<Course> unpublishCourse(@PathVariable String id) {
        return ResponseEntity.ok(courseService.unpublishCourse(id));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/{courseId}/videos/{videoId}")
    public ResponseEntity<Course> addVideoToCourse(
            @PathVariable String courseId,
            @PathVariable String videoId) {
        return ResponseEntity.ok(courseService.addVideoToCourse(courseId, videoId));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @DeleteMapping("/{courseId}/videos/{videoId}")
    public ResponseEntity<Course> removeVideoFromCourse(
            @PathVariable String courseId,
            @PathVariable String videoId) {
        return ResponseEntity.ok(courseService.removeVideoFromCourse(courseId, videoId));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/{courseId}/documents/{documentId}")
    public ResponseEntity<Course> addDocumentToCourse(
            @PathVariable String courseId,
            @PathVariable String documentId) {
        return ResponseEntity.ok(courseService.addDocumentToCourse(courseId, documentId));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @DeleteMapping("/{courseId}/documents/{documentId}")
    public ResponseEntity<Course> removeDocumentFromCourse(
            @PathVariable String courseId,
            @PathVariable String documentId) {
        return ResponseEntity.ok(courseService.removeDocumentFromCourse(courseId, documentId));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/{courseId}/quizzes/{quizId}")
    public ResponseEntity<Course> addQuizToCourse(
            @PathVariable String courseId,
            @PathVariable String quizId) {
        return ResponseEntity.ok(courseService.addQuizToCourse(courseId, quizId));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @DeleteMapping("/{courseId}/quizzes/{quizId}")
    public ResponseEntity<Course> removeQuizFromCourse(
            @PathVariable String courseId,
            @PathVariable String quizId) {
        return ResponseEntity.ok(courseService.removeQuizFromCourse(courseId, quizId));
    }
}