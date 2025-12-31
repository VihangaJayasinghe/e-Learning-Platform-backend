package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Course;
import com.Learn.ELP_backend.model.Review;
import com.Learn.ELP_backend.repository.CourseRepository;
import com.Learn.ELP_backend.repository.ReviewRepository;
import com.Learn.ELP_backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Override
    public Review createReview(Review review) {
        // Check if student has already reviewed this course
        if (hasStudentReviewed(review.getCourseId(), review.getStudentId())) {
            throw new RuntimeException("You have already reviewed this course");
        }
        
        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        
        // Set timestamps
        review.setId(UUID.randomUUID().toString());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        
        // Set instructor ID from course
        Course course = courseRepository.findById(review.getCourseId()).orElse(null);
        if (course != null) {
            review.setInstructorId(course.getInstructorId());
        }
        
        // Save review
        Review savedReview = reviewRepository.save(review);
        
        // Update course rating
        updateCourseRatingFromReviews(review.getCourseId());
        
        return savedReview;
    }
    
    @Override
    public Review updateReview(String id, Review reviewUpdate) {
        Review review = getReviewById(id);
        
        if (reviewUpdate.getRating() != null) {
            if (reviewUpdate.getRating() < 1 || reviewUpdate.getRating() > 5) {
                throw new RuntimeException("Rating must be between 1 and 5");
            }
            review.setRating(reviewUpdate.getRating());
        }
        
        if (reviewUpdate.getTitle() != null) {
            review.setTitle(reviewUpdate.getTitle());
        }
        
        if (reviewUpdate.getComment() != null) {
            review.setComment(reviewUpdate.getComment());
        }
        
        review.setUpdatedAt(LocalDateTime.now());
        
        Review updatedReview = reviewRepository.save(review);
        
        // Update course rating
        updateCourseRatingFromReviews(review.getCourseId());
        
        return updatedReview;
    }
    
    @Override
    public void deleteReview(String id) {
        Review review = getReviewById(id);
        String courseId = review.getCourseId();
        
        reviewRepository.deleteById(id);
        
        // Update course rating
        updateCourseRatingFromReviews(courseId);
    }
    
    @Override
    public Review getReviewById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found: " + id));
    }
    
    @Override
    public List<Review> getReviewsForCourse(String courseId) {
        return reviewRepository.findByCourseId(courseId);
    }
    
    @Override
    public List<Review> getApprovedReviewsForCourse(String courseId) {
        return reviewRepository.findByCourseIdAndIsApproved(courseId, true);
    }
    
    @Override
    public List<Review> getStudentReviews(String studentId) {
        return reviewRepository.findByStudentId(studentId);
    }
    
    @Override
    public List<Review> getInstructorReviews(String instructorId) {
        return reviewRepository.findByInstructorId(instructorId);
    }
    
    @Override
    public boolean hasStudentReviewed(String courseId, String studentId) {
        return reviewRepository.existsByCourseIdAndStudentId(courseId, studentId);
    }
    
    @Override
    public Review voteHelpful(String reviewId) {
        Review review = getReviewById(reviewId);
        review.setHelpfulVotes(review.getHelpfulVotes() + 1);
        return reviewRepository.save(review);
    }
    
    @Override
    public Review reportReview(String reviewId) {
        Review review = getReviewById(reviewId);
        // For now, just mark as not approved if reported
        // In real app, you'd track reports separately
        review.setIsApproved(false);
        return reviewRepository.save(review);
    }
    
    @Override
    public void approveReview(String reviewId) {
        Review review = getReviewById(reviewId);
        review.setIsApproved(true);
        reviewRepository.save(review);
        
        // Update course rating
        updateCourseRatingFromReviews(review.getCourseId());
    }
    
    @Override
    public void rejectReview(String reviewId) {
        Review review = getReviewById(reviewId);
        review.setIsApproved(false);
        reviewRepository.save(review);
        
        // Update course rating
        updateCourseRatingFromReviews(review.getCourseId());
    }
    
    @Override
    public Map<String, Object> getCourseReviewStats(String courseId) {
        List<Review> approvedReviews = getApprovedReviewsForCourse(courseId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", approvedReviews.size());
        
        if (approvedReviews.isEmpty()) {
            stats.put("averageRating", 0.0);
            stats.put("ratingDistribution", Map.of(1, 0, 2, 0, 3, 0, 4, 0, 5, 0));
            return stats;
        }
        
        // Calculate average rating
        double average = approvedReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        stats.put("averageRating", Math.round(average * 10.0) / 10.0);
        
        // Calculate rating distribution
        Map<Integer, Long> distribution = approvedReviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));
        
        // Ensure all ratings 1-5 are in the map
        for (int i = 1; i <= 5; i++) {
            distribution.putIfAbsent(i, 0L);
        }
        stats.put("ratingDistribution", distribution);
        
        return stats;
    }
    
    @Override
    public void updateCourseRatingFromReviews(String courseId) {
        List<Review> approvedReviews = getApprovedReviewsForCourse(courseId);
        
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return;
        }
        
        if (approvedReviews.isEmpty()) {
            course.setAverageRating(0.0);
            course.setTotalReviews(0);
        } else {
            double average = approvedReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            
            course.setAverageRating(Math.round(average * 10.0) / 10.0);
            course.setTotalReviews(approvedReviews.size());
        }
        
        courseRepository.save(course);
    }
}