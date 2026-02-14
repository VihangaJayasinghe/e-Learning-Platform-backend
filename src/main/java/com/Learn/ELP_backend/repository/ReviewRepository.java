package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    // For both courses and classes
    List<Review> findByTargetId(String targetId);
    List<Review> findByTargetIdAndTargetType(String targetId, Review.ReviewTargetType targetType);
    List<Review> findByTargetIdAndTargetTypeAndIsApproved(String targetId, 
                                                         Review.ReviewTargetType targetType, 
                                                         boolean isApproved);
    
    // Student reviews
    List<Review> findByStudentId(String studentId);
    
    // Instructor reviews (both courses and classes they teach)
    List<Review> findByInstructorId(String instructorId);
    
    // Check if student has already reviewed
    boolean existsByTargetIdAndStudentId(String targetId, String studentId);
    
    // Count reviews
    Integer countByTargetIdAndTargetTypeAndIsApproved(String targetId, 
                                                     Review.ReviewTargetType targetType, 
                                                     boolean isApproved);
}