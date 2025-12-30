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
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Review>> getReviewsForCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(reviewService.getReviewsForCourse(courseId));
    }
    
    @GetMapping("/course/{courseId}/approved")
    public ResponseEntity<List<Review>> getApprovedReviewsForCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsForCourse(courseId));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Review>> getStudentReviews(@PathVariable String studentId) {
        return ResponseEntity.ok(reviewService.getStudentReviews(studentId));
    }
    
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Review>> getInstructorReviews(@PathVariable String instructorId) {
        return ResponseEntity.ok(reviewService.getInstructorReviews(instructorId));
    }
    
    @GetMapping("/course/{courseId}/student/{studentId}/has-reviewed")
    public ResponseEntity<Boolean> hasStudentReviewed(
            @PathVariable String courseId,
            @PathVariable String studentId) {
        return ResponseEntity.ok(reviewService.hasStudentReviewed(courseId, studentId));
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
    
    @GetMapping("/course/{courseId}/stats")
    public ResponseEntity<Map<String, Object>> getCourseReviewStats(@PathVariable String courseId) {
        return ResponseEntity.ok(reviewService.getCourseReviewStats(courseId));
    }
}