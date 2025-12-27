package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.ClassStatus;
import com.Learn.ELP_backend.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    // Class CRUD operations
    @PostMapping
    public ResponseEntity<Class> createClass(@RequestBody Class classObj) {
        Class createdClass = classService.createClass(classObj);
        return ResponseEntity.ok(createdClass);
    }

    @GetMapping
    public ResponseEntity<List<Class>> getAllClasses() {
        List<Class> classes = classService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Class> getClassById(@PathVariable String id) {
        Class classObj = classService.getClassById(id);
        return ResponseEntity.ok(classObj);
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Class>> getClassesByInstructor(@PathVariable String instructorId) {
        List<Class> classes = classService.getClassesByInstructor(instructorId);
        return ResponseEntity.ok(classes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Class> updateClass(@PathVariable String id, @RequestBody Class classUpdate) {
        Class updatedClass = classService.updateClass(id, classUpdate);
        return ResponseEntity.ok(updatedClass);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable String id) {
        classService.deleteClass(id);
        return ResponseEntity.ok().build();
    }

    // Class status management
    @PatchMapping("/{id}/status")
    public ResponseEntity<Class> updateClassStatus(
            @PathVariable String id, 
            @RequestParam ClassStatus status) {
        Class updatedClass = classService.updateClassStatus(id, status);
        return ResponseEntity.ok(updatedClass);
    }

    // Month video management
    @PostMapping("/{classId}/months/{yearMonth}/videos/{videoId}")
    public ResponseEntity<Class> addVideoToMonth(
            @PathVariable String classId,
            @PathVariable String yearMonth,
            @PathVariable String videoId) {
        Class updatedClass = classService.addVideoToMonth(classId, yearMonth, videoId);
        return ResponseEntity.ok(updatedClass);
    }

    @DeleteMapping("/{classId}/months/{yearMonth}/videos/{videoId}")
    public ResponseEntity<Class> removeVideoFromMonth(
            @PathVariable String classId,
            @PathVariable String yearMonth,
            @PathVariable String videoId) {
        Class updatedClass = classService.removeVideoFromMonth(classId, yearMonth, videoId);
        return ResponseEntity.ok(updatedClass);
    }

    // Month document management
    @PostMapping("/{classId}/months/{yearMonth}/documents/{documentId}")
    public ResponseEntity<Class> addDocumentToMonth(
        @PathVariable String classId,
        @PathVariable String yearMonth,
        @PathVariable String documentId) {
    Class updatedClass = classService.addDocumentToMonth(classId, yearMonth, documentId);
    return ResponseEntity.ok(updatedClass);
}

    @DeleteMapping("/{classId}/months/{yearMonth}/documents/{documentId}")
    public ResponseEntity<Class> removeDocumentFromMonth(
        @PathVariable String classId,
        @PathVariable String yearMonth,
        @PathVariable String documentId) {
    Class updatedClass = classService.removeDocumentFromMonth(classId, yearMonth, documentId);
    return ResponseEntity.ok(updatedClass);
} 

    // Month release management
    @PostMapping("/{classId}/months/{yearMonth}/release")
    public ResponseEntity<Class> releaseMonth(
            @PathVariable String classId,
            @PathVariable String yearMonth) {
        Class updatedClass = classService.releaseMonth(classId, yearMonth);
        return ResponseEntity.ok(updatedClass);
    }

    @PostMapping("/{classId}/months/{yearMonth}/unrelease")
    public ResponseEntity<Class> unreleaseMonth(
            @PathVariable String classId,
            @PathVariable String yearMonth) {
        Class updatedClass = classService.unreleaseMonth(classId, yearMonth);
        return ResponseEntity.ok(updatedClass);
    }

    // Class duration management
    @PatchMapping("/{id}/extend")
    public ResponseEntity<Class> extendClassDuration(
            @PathVariable String id,
            @RequestParam int additionalMonths) {
        Class updatedClass = classService.extendClassDuration(id, additionalMonths);
        return ResponseEntity.ok(updatedClass);
    }

    // Get month's videos
    @GetMapping("/{classId}/months/{yearMonth}/videos")
    public ResponseEntity<List<String>> getMonthVideos(
            @PathVariable String classId,
            @PathVariable String yearMonth) {
        List<String> videoIds = classService.getMonthVideos(classId, yearMonth);
        return ResponseEntity.ok(videoIds);
    }

    // Get month's documents
    @GetMapping("/{classId}/months/{yearMonth}/documents")
    public ResponseEntity<List<String>> getMonthDocuments(
        @PathVariable String classId,
        @PathVariable String yearMonth) {
    List<String> documentIds = classService.getMonthDocuments(classId, yearMonth);
    return ResponseEntity.ok(documentIds);
    }
}