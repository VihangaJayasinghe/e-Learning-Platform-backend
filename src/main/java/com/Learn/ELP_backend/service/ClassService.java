package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.ClassStatus;

import java.util.List;

public interface ClassService {

    // Class CRUD operations
    Class createClass(Class classObj);

    List<Class> getAllClasses();

    Class getClassById(String id);

    List<Class> getClassesByInstructor(String username);

    Class updateClass(String id, Class classUpdate);

    void deleteClass(String id);

    // Class status management
    Class updateClassStatus(String id, ClassStatus status);

    // Month management
    Class addVideoToMonth(String classId, String yearMonth, String videoId);

    Class removeVideoFromMonth(String classId, String yearMonth, String videoId);

    Class releaseMonth(String classId, String yearMonth);

    Class unreleaseMonth(String classId, String yearMonth);

    
    // Class duration management
    Class extendClassDuration(String classId, int additionalMonths);

    // Get specific month's videos
    List<String> getMonthVideos(String classId, String yearMonth);

    // Month document management
    Class addDocumentToMonth(String classId, String yearMonth, String documentId);
    Class removeDocumentFromMonth(String classId, String yearMonth, String documentId);

    // Get specific month's documents
    List<String> getMonthDocuments(String classId, String yearMonth);





}