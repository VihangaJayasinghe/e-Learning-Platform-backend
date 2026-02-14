package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findByInstructorId(String instructorId);
    List<Course> findBySubject(String subject);
    List<Course> findByTopic(String topic);
    List<Course> findByLevel(String level);
    List<Course> findByIsPublished(boolean isPublished);
    List<Course> findByIsFree(boolean isFree);
    List<Course> findByTagsContaining(String tag);
    List<Course> findByCourseTitleContainingIgnoreCase(String title);
}