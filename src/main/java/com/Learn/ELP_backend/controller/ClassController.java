package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.ClassStatus;
import com.Learn.ELP_backend.service.ClassService;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Class> createClass(@RequestBody Class classObj, Principal principal) {
        String username = principal.getName();
        User teacher = userRepository.findByUsername(username);
        classObj.setTeacher(teacher);

        Class createdClass = classService.createClass(classObj);
        return ResponseEntity.ok(createdClass);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<List<Class>> getAllClasses() {
        List<Class> classes = classService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Class> getClassById(@PathVariable String id) {
        Class classObj = classService.getClassById(id);
        return ResponseEntity.ok(classObj);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/instructor/{username}")
    public ResponseEntity<List<Class>> getClassesByInstructor(@PathVariable String username) {
        List<Class> classes = classService.getClassesByInstructor(username);
        return ResponseEntity.ok(classes);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Class> updateClass(@PathVariable String id, @RequestBody Class classUpdate) {
        Class updatedClass = classService.updateClass(id, classUpdate);
        return ResponseEntity.ok(updatedClass);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable String id) {
        classService.deleteClass(id);
        return ResponseEntity.ok().build();
    }

    // Class status management
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Class> updateClassStatus(
            @PathVariable String id,
            @RequestParam ClassStatus status) {
        Class updatedClass = classService.updateClassStatus(id, status);
        return ResponseEntity.ok(updatedClass);
    }

    // Month video management
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping("/{classId}/months/{yearMonth}/videos/{videoId}")
    public ResponseEntity<Class> addVideoToMonth(
            @PathVariable String classId,
            @PathVariable String yearMonth,
            @PathVariable String videoId) {
        Class updatedClass = classService.addVideoToMonth(classId, yearMonth, videoId);
        return ResponseEntity.ok(updatedClass);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping("/{classId}/months/{yearMonth}/release")
    public ResponseEntity<Class> releaseMonth(
            @PathVariable String classId,
            @PathVariable String yearMonth) {
        Class updatedClass = classService.releaseMonth(classId, yearMonth);
        return ResponseEntity.ok(updatedClass);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping("/{classId}/months/{yearMonth}/unrelease")
    public ResponseEntity<Class> unreleaseMonth(
            @PathVariable String classId,
            @PathVariable String yearMonth) {
        Class updatedClass = classService.unreleaseMonth(classId, yearMonth);
        return ResponseEntity.ok(updatedClass);
    }

    // Class duration management
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/extend")
    public ResponseEntity<Class> extendClassDuration(
            @PathVariable String id,
            @RequestParam int additionalMonths) {
        Class updatedClass = classService.extendClassDuration(id, additionalMonths);
        return ResponseEntity.ok(updatedClass);
    }

    // Get month's videos
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('STUDENT')")
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