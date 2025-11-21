package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.ClassMonth;
import com.Learn.ELP_backend.model.ClassStatus;
import com.Learn.ELP_backend.repository.ClassRepository;
import com.Learn.ELP_backend.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Override
    public Class createClass(Class classObj) {
        classObj.initializeMonths();
        return classRepository.save(classObj);
    }

    @Override
    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }

    @Override
    public Class getClassById(String id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
    }

    @Override
    public List<Class> getClassesByInstructor(String instructorId) {
        return classRepository.findByInstructorId(instructorId);
    }

    @Override
    public Class updateClass(String id, Class classUpdate) {
        Class existingClass = getClassById(id);
        
        if (classUpdate.getClassName() != null) {
            existingClass.setClassName(classUpdate.getClassName());
        }
        if (classUpdate.getDescription() != null) {
            existingClass.setDescription(classUpdate.getDescription());
        }
        if (classUpdate.getMonthlyPrice() != null) {
            existingClass.setMonthlyPrice(classUpdate.getMonthlyPrice());
        }
        
        return classRepository.save(existingClass);
    }

    @Override
    public void deleteClass(String id) {
        Class classObj = getClassById(id);
        classRepository.delete(classObj);
    }

    @Override
    public Class updateClassStatus(String id, ClassStatus status) {
        Class classObj = getClassById(id);
        classObj.setStatus(status);
        return classRepository.save(classObj);
    }

    @Override
    public Class addVideoToMonth(String classId, String yearMonth, String videoId) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);
        
        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }
        
        if (!month.getVideoIds().contains(videoId)) {
            month.getVideoIds().add(videoId);
        }
        
        return classRepository.save(classObj);
    }

    @Override
    public Class removeVideoFromMonth(String classId, String yearMonth, String videoId) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);
        
        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }
        
        month.getVideoIds().remove(videoId);
        return classRepository.save(classObj);
    }

    @Override
    public Class releaseMonth(String classId, String yearMonth) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);
        
        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }
        
        month.setReleased(true);
        month.setReleaseDate(LocalDateTime.now());
        
        return classRepository.save(classObj);
    }

    @Override
    public Class unreleaseMonth(String classId, String yearMonth) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);
        
        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }
        
        month.setReleased(false);
        month.setReleaseDate(null);
        
        return classRepository.save(classObj);
    }

    @Override
    public Class extendClassDuration(String classId, int additionalMonths) {
        if (additionalMonths <= 0) {
            throw new RuntimeException("Additional months must be positive");
        }
        
        Class classObj = getClassById(classId);
        classObj.extendDuration(additionalMonths);
        
        return classRepository.save(classObj);
    }

    @Override
    public List<String> getMonthVideos(String classId, String yearMonth) {
        Class classObj = getClassById(classId);
        ClassMonth month = classObj.findMonth(yearMonth);
        
        if (month == null) {
            throw new RuntimeException("Month not found in class: " + yearMonth);
        }
        
        return month.getVideoIds();
    }
}