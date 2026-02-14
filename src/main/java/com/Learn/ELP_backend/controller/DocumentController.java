package com.Learn.ELP_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Learn.ELP_backend.model.Documents;
import com.Learn.ELP_backend.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/upload")
    public ResponseEntity<Documents> uploadDocument(
        @RequestParam("file") MultipartFile file,
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("uploadedBy") String uploadedBy,
        @RequestParam("classId") String classId) {
    Documents doc = documentService.uploadDocument(file, name, description, uploadedBy, classId);
        return ResponseEntity.ok(doc);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Documents>> getDocumentsByClass(@PathVariable String classId) {
        List<Documents> docs = documentService.getDocumentsByClass(classId);
        return ResponseEntity.ok(docs);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Documents> getDocumentById(@PathVariable String id) {
        Documents doc = documentService.getDocumentById(id);
        if (doc != null) return ResponseEntity.ok(doc);
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

}
