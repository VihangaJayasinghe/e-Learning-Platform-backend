package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Review;
import com.Learn.ELP_backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody Review reviewUpdate) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewUpdate));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }
    
    // Get reviews for a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Review>> getCourseReviews(@PathVariable String courseId) {
        return ResponseEntity.ok(reviewService.getReviewsForTarget(courseId, Review.ReviewTargetType.COURSE));
    }
    
    // Get reviews for a class
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Review>> getClassReviews(@PathVariable String classId) {
        return ResponseEntity.ok(reviewService.getReviewsForTarget(classId, Review.ReviewTargetType.CLASS));
    }
    
    // Get approved reviews for a course
    @GetMapping("/course/{courseId}/approved")
    public ResponseEntity<List<Review>> getApprovedCourseReviews(@PathVariable String courseId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsForTarget(courseId, Review.ReviewTargetType.COURSE));
    }
    
    // Get approved reviews for a class
    @GetMapping("/class/{classId}/approved")
    public ResponseEntity<List<Review>> getApprovedClassReviews(@PathVariable String classId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsForTarget(classId, Review.ReviewTargetType.CLASS));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Review>> getStudentReviews(@PathVariable String studentId) {
        return ResponseEntity.ok(reviewService.getStudentReviews(studentId));
    }
    
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Review>> getInstructorReviews(@PathVariable String instructorId) {
        return ResponseEntity.ok(reviewService.getInstructorReviews(instructorId));
    }
    
    // Check if student has reviewed a course
    @GetMapping("/course/{courseId}/student/{studentId}/has-reviewed")
    public ResponseEntity<Boolean> hasStudentReviewedCourse(
            @PathVariable String courseId,
            @PathVariable String studentId) {
        return ResponseEntity.ok(reviewService.hasStudentReviewed(courseId, studentId));
    }
    
    // Check if student has reviewed a class
    @GetMapping("/class/{classId}/student/{studentId}/has-reviewed")
    public ResponseEntity<Boolean> hasStudentReviewedClass(
            @PathVariable String classId,
            @PathVariable String studentId) {
        return ResponseEntity.ok(reviewService.hasStudentReviewed(classId, studentId));
    }
    
    @PostMapping("/{reviewId}/helpful")
    public ResponseEntity<Review> voteHelpful(@PathVariable String reviewId) {
        return ResponseEntity.ok(reviewService.voteHelpful(reviewId));
    }
    
    @PostMapping("/{reviewId}/report")
    public ResponseEntity<Review> reportReview(@PathVariable String reviewId) {
        return ResponseEntity.ok(reviewService.reportReview(reviewId));
    }
    
    @PostMapping("/{reviewId}/approve")
    public ResponseEntity<Void> approveReview(@PathVariable String reviewId) {
        reviewService.approveReview(reviewId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{reviewId}/reject")
    public ResponseEntity<Void> rejectReview(@PathVariable String reviewId) {
        reviewService.rejectReview(reviewId);
        return ResponseEntity.ok().build();
    }
    
    // Get review statistics for a course
    @GetMapping("/course/{courseId}/stats")
    public ResponseEntity<Map<String, Object>> getCourseReviewStats(@PathVariable String courseId) {
        return ResponseEntity.ok(reviewService.getReviewStats(courseId, Review.ReviewTargetType.COURSE));
    }
    
    // Get review statistics for a class
    @GetMapping("/class/{classId}/stats")
    public ResponseEntity<Map<String, Object>> getClassReviewStats(@PathVariable String classId) {
        return ResponseEntity.ok(reviewService.getReviewStats(classId, Review.ReviewTargetType.CLASS));
    }
}