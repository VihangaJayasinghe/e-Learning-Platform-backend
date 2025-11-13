package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.ClassStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends MongoRepository<Class, String> {
    
    List<Class> findByInstructorId(String instructorId);
    
    List<Class> findByStatus(ClassStatus status);
    
    List<Class> findByInstructorIdAndStatus(String instructorId, ClassStatus status);
    
    // Find classes that have a specific month
    @Query("{ 'months.yearMonth': ?0 }")
    List<Class> findByMonth(String yearMonth);
    
    // Find classes where specific month is released
    @Query("{ 'months': { $elemMatch: { 'yearMonth': ?0, 'isReleased': true } } }")
    List<Class> findByMonthAndReleased(String yearMonth);
    
    Optional<Class> findByIdAndInstructorId(String id, String instructorId);
}