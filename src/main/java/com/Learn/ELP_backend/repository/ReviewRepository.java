package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByCourseId(String courseId);
    List<Review> findByCourseIdAndIsApproved(String courseId, boolean isApproved);
    List<Review> findByStudentId(String studentId);
    List<Review> findByInstructorId(String instructorId);
    List<Review> findByRating(Integer rating);
    boolean existsByCourseIdAndStudentId(String courseId, String studentId);
    Integer countByCourseIdAndIsApproved(String courseId, boolean isApproved);
}