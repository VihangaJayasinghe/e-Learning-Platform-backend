package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.*;
import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.repository.*;
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
    
    @Autowired
    private ClassRepository classRepository;
    
    @Override
    public Review createReview(Review review) {
        // Check if student has already reviewed this target
        if (hasStudentReviewed(review.getTargetId(), review.getStudentId())) {
            throw new RuntimeException("You have already reviewed this " + 
                                      review.getTargetType().toString().toLowerCase());
        }
        
        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        
        // Set timestamps
        review.setId(UUID.randomUUID().toString());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        
        // Set instructor ID based on target type
        if (review.getTargetType() == Review.ReviewTargetType.COURSE) {
            Course course = courseRepository.findById(review.getTargetId()).orElse(null);
            if (course != null) {
                review.setInstructorId(course.getInstructorId());
            }
        } else if (review.getTargetType() == Review.ReviewTargetType.CLASS) {
            Class classObj = classRepository.findById(review.getTargetId()).orElse(null);
            if (classObj != null) {
                review.setInstructorId(classObj.getInstructorId());
            }
        }
        
        // Save review
        Review savedReview = reviewRepository.save(review);
        
        // Update target rating
        updateTargetRating(review.getTargetId(), review.getTargetType());
        
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
        
        // Update target rating
        updateTargetRating(review.getTargetId(), review.getTargetType());
        
        return updatedReview;
    }
    
    @Override
    public void deleteReview(String id) {
        Review review = getReviewById(id);
        String targetId = review.getTargetId();
        Review.ReviewTargetType targetType = review.getTargetType();
        
        reviewRepository.deleteById(id);
        
        // Update target rating
        updateTargetRating(targetId, targetType);
    }
    
    @Override
    public Review getReviewById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found: " + id));
    }
    
    @Override
    public List<Review> getReviewsForTarget(String targetId, Review.ReviewTargetType targetType) {
        return reviewRepository.findByTargetIdAndTargetType(targetId, targetType);
    }
    
    @Override
    public List<Review> getApprovedReviewsForTarget(String targetId, Review.ReviewTargetType targetType) {
        return reviewRepository.findByTargetIdAndTargetTypeAndIsApproved(targetId, targetType, true);
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
    public boolean hasStudentReviewed(String targetId, String studentId) {
        return reviewRepository.existsByTargetIdAndStudentId(targetId, studentId);
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
        review.setIsApproved(false);
        return reviewRepository.save(review);
    }
    
    @Override
    public void approveReview(String reviewId) {
        Review review = getReviewById(reviewId);
        review.setIsApproved(true);
        reviewRepository.save(review);
        
        // Update target rating
        updateTargetRating(review.getTargetId(), review.getTargetType());
    }
    
    @Override
    public void rejectReview(String reviewId) {
        Review review = getReviewById(reviewId);
        review.setIsApproved(false);
        reviewRepository.save(review);
        
        // Update target rating
        updateTargetRating(review.getTargetId(), review.getTargetType());
    }
    
    @Override
    public Map<String, Object> getReviewStats(String targetId, Review.ReviewTargetType targetType) {
        List<Review> approvedReviews = getApprovedReviewsForTarget(targetId, targetType);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", approvedReviews.size());
        stats.put("targetType", targetType.toString());
        
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
    public void updateTargetRating(String targetId, Review.ReviewTargetType targetType) {
        List<Review> approvedReviews = getApprovedReviewsForTarget(targetId, targetType);
        
        if (targetType == Review.ReviewTargetType.COURSE) {
            updateCourseRating(targetId, approvedReviews);
        } else if (targetType == Review.ReviewTargetType.CLASS) {
            updateClassRating(targetId, approvedReviews);
        }
    }
    
    private void updateCourseRating(String courseId, List<Review> approvedReviews) {
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
    
    private void updateClassRating(String classId, List<Review> approvedReviews) {
        Class classObj = classRepository.findById(classId).orElse(null);
        if (classObj == null) {
            return;
        }
        
        if (approvedReviews.isEmpty()) {
            classObj.setAverageRating(0.0);
            classObj.setTotalReviews(0);
        } else {
            double average = approvedReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            
            classObj.setAverageRating(Math.round(average * 10.0) / 10.0);
            classObj.setTotalReviews(approvedReviews.size());
        }
        
        classRepository.save(classObj);
    }
}