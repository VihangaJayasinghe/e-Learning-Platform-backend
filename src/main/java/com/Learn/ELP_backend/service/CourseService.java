package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Course;
import java.util.List;

public interface CourseService {
    Course createCourse(Course course);
    Course updateCourse(String id, Course courseUpdate);
    void deleteCourse(String id);
    Course getCourseById(String id);
    Course getPublishedCourseById(String id);
    List<Course> getAllCourses();
    List<Course> getPublishedCourses();
    List<Course> getCoursesByInstructor(String instructorId);
    List<Course> searchCourses(String keyword, String subject, String topic, String level, Boolean isFree);
    Course publishCourse(String id);
    Course unpublishCourse(String id);
    
    // Content management
    Course addVideoToCourse(String courseId, String videoId);
    Course removeVideoFromCourse(String courseId, String videoId);
    Course addDocumentToCourse(String courseId, String documentId);
    Course removeDocumentFromCourse(String courseId, String documentId);
    Course addQuizToCourse(String courseId, String quizId);
    Course removeQuizFromCourse(String courseId, String quizId);
    
    // Update rating from reviews
    void updateCourseRating(String courseId);
}
