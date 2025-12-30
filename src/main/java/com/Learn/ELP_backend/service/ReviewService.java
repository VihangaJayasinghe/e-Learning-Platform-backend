package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Review;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    Review createReview(Review review);
    Review updateReview(String id, Review reviewUpdate);
    void deleteReview(String id);
    Review getReviewById(String id);
    List<Review> getReviewsForCourse(String courseId);
    List<Review> getApprovedReviewsForCourse(String courseId);
    List<Review> getStudentReviews(String studentId);
    List<Review> getInstructorReviews(String instructorId);
    boolean hasStudentReviewed(String courseId, String studentId);
    Review voteHelpful(String reviewId);
    Review reportReview(String reviewId);
    void approveReview(String reviewId);
    void rejectReview(String reviewId);
    Map<String, Object> getCourseReviewStats(String courseId);
    void updateCourseRatingFromReviews(String courseId);
}