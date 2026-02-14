package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Review;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    // Create/Update/Delete
    Review createReview(Review review);
    Review updateReview(String id, Review reviewUpdate);
    void deleteReview(String id);
    Review getReviewById(String id);
    
    // Get reviews
    List<Review> getReviewsForTarget(String targetId, Review.ReviewTargetType targetType);
    List<Review> getApprovedReviewsForTarget(String targetId, Review.ReviewTargetType targetType);
    List<Review> getStudentReviews(String studentId);
    List<Review> getInstructorReviews(String instructorId);
    
    // Check if reviewed
    boolean hasStudentReviewed(String targetId, String studentId);
    
    // Review actions
    Review voteHelpful(String reviewId);
    Review reportReview(String reviewId);
    void approveReview(String reviewId);
    void rejectReview(String reviewId);
    
    // Stats
    Map<String, Object> getReviewStats(String targetId, Review.ReviewTargetType targetType);
    
    // Update target ratings (for both courses and classes)
    void updateTargetRating(String targetId, Review.ReviewTargetType targetType);
}