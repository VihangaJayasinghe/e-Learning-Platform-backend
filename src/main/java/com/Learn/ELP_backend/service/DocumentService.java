package com.Learn.ELP_backend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.Learn.ELP_backend.model.Documents;


public interface DocumentService {
    Documents uploadDocument(MultipartFile file, String name, String description, String uploadedBy, String classId);
    
    List<Documents> getDocumentsByClass(String classId);
    Documents getDocumentById(String id);
    void deleteDocument(String id);
}
