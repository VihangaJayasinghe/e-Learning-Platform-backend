package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Course;
import com.Learn.ELP_backend.repository.CourseRepository;
import com.Learn.ELP_backend.service.CourseService;
import com.Learn.ELP_backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private ReviewService reviewService;

    @Override
    public Course createCourse(Course course) {
        // Generate ID if not provided
        if (course.getId() == null || course.getId().isEmpty()) {
            course.setId(UUID.randomUUID().toString());
        }
        
        // Set timestamps
        course.setCreatedAt(LocalDateTime.now());
        
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(String id, Course courseUpdate) {
        Course course = getCourseById(id);
        
        if (courseUpdate.getCourseTitle() != null) {
            course.setCourseTitle(courseUpdate.getCourseTitle());
        }
        if (courseUpdate.getDescription() != null) {
            course.setDescription(courseUpdate.getDescription());
        }
        if (courseUpdate.getSubject() != null) {
            course.setSubject(courseUpdate.getSubject());
        }
        if (courseUpdate.getTopic() != null) {
            course.setTopic(courseUpdate.getTopic());
        }
        if (courseUpdate.getLevel() != null) {
            course.setLevel(courseUpdate.getLevel());
        }
        if (courseUpdate.getInstructorName() != null) {
            course.setInstructorName(courseUpdate.getInstructorName());
        }
        if (courseUpdate.getEstimatedHours() != null) {
            course.setEstimatedHours(courseUpdate.getEstimatedHours());
        }
        if (courseUpdate.getTotalLessons() != null) {
            course.setTotalLessons(courseUpdate.getTotalLessons());
        }
        if (courseUpdate.getPrice() != null) {
            course.setPrice(courseUpdate.getPrice());
        }
        if (courseUpdate.getIsFree() != null) {
            course.setIsFree(courseUpdate.getIsFree());
        }
        if (courseUpdate.getTags() != null) {
            course.setTags(courseUpdate.getTags());
        }
        if (courseUpdate.getThumbnailUrl() != null) {
            course.setThumbnailUrl(courseUpdate.getThumbnailUrl());
        }
        if (courseUpdate.getPreviewVideoUrl() != null) {
            course.setPreviewVideoUrl(courseUpdate.getPreviewVideoUrl());
        }
        
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(String id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    @Override
    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
    }

    @Override
    public Course getPublishedCourseById(String id) {
        Course course = getCourseById(id);
        if (!course.getIsPublished()) {
            throw new RuntimeException("Course is not published");
        }
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    @Override
    public List<Course> getPublishedCourses() {
        return courseRepository.findByIsPublished(true);
    }
    
    @Override
    public List<Course> getCoursesByInstructor(String instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    @Override
    public List<Course> searchCourses(String keyword, String subject, String topic, 
                                     String level, Boolean isFree) {
        List<Course> results = getPublishedCourses();
        
        // Filter by keyword (title contains)
        if (keyword != null && !keyword.isEmpty()) {
            results = results.stream()
                    .filter(course -> course.getCourseTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                                     course.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }
        
        // Filter by subject
        if (subject != null && !subject.isEmpty()) {
            results = results.stream()
                    .filter(course -> subject.equalsIgnoreCase(course.getSubject()))
                    .toList();
        }
        
        // Filter by topic
        if (topic != null && !topic.isEmpty()) {
            results = results.stream()
                    .filter(course -> topic.equalsIgnoreCase(course.getTopic()))
                    .toList();
        }
        
        // Filter by level
        if (level != null && !level.isEmpty()) {
            results = results.stream()
                    .filter(course -> level.equalsIgnoreCase(course.getLevel()))
                    .toList();
        }
        
        // Filter by free/paid
        if (isFree != null) {
            results = results.stream()
                    .filter(course -> isFree.equals(course.getIsFree()))
                    .toList();
        }
        
        return results;
    }

    @Override
    public Course publishCourse(String id) {
        Course course = getCourseById(id);
        course.setIsPublished(true);
        course.setPublishedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }
    
    @Override
    public Course unpublishCourse(String id) {
        Course course = getCourseById(id);
        course.setIsPublished(false);
        return courseRepository.save(course);
    }

    @Override
    public Course addVideoToCourse(String courseId, String videoId) {
        Course course = getCourseById(courseId);
        
        if (!course.getVideoIds().contains(videoId)) {
            course.getVideoIds().add(videoId);
        }
        
        return courseRepository.save(course);
    }
    
    @Override
    public Course removeVideoFromCourse(String courseId, String videoId) {
        Course course = getCourseById(courseId);
        course.getVideoIds().remove(videoId);
        return courseRepository.save(course);
    }
    
    @Override
    public Course addDocumentToCourse(String courseId, String documentId) {
        Course course = getCourseById(courseId);
        
        if (!course.getDocumentIds().contains(documentId)) {
            course.getDocumentIds().add(documentId);
        }
        
        return courseRepository.save(course);
    }
    
    @Override
    public Course removeDocumentFromCourse(String courseId, String documentId) {
        Course course = getCourseById(courseId);
        course.getDocumentIds().remove(documentId);
        return courseRepository.save(course);
    }
    
    @Override
    public Course addQuizToCourse(String courseId, String quizId) {
        Course course = getCourseById(courseId);
        
        if (!course.getQuizIds().contains(quizId)) {
            course.getQuizIds().add(quizId);
        }
        
        return courseRepository.save(course);
    }
    
    @Override
    public Course removeQuizFromCourse(String courseId, String quizId) {
        Course course = getCourseById(courseId);
        course.getQuizIds().remove(quizId);
        return courseRepository.save(course);
    }

    @Override
    public void updateCourseRating(String courseId) {
        // This will be called from ReviewService when reviews are added/updated/deleted
        // For now, we'll just update it when needed
        // In the ReviewService, we'll call this method
    }

}
