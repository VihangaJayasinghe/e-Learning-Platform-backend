package com.Learn.ELP_backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Learn.ELP_backend.model.Documents;
import com.Learn.ELP_backend.repository.DocumentRepository;
import com.Learn.ELP_backend.service.CloudinaryStorageService;
import com.Learn.ELP_backend.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService{
@Autowired
private DocumentRepository documentRepository;

@Autowired
private CloudinaryStorageService cloudinaryStorageService;

@Override
public Documents uploadDocument(MultipartFile file, String name, String description, String uploadedBy, String classId) {
    try {
            if (file.isEmpty()) throw new RuntimeException("File is empty");

            String contentType = file.getContentType();
            if (!contentType.equals("application/pdf") &&
                !contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation") &&
                !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                throw new RuntimeException("Invalid file type");
                }
         // Upload to Cloudinary as raw
            String cloudinaryUrl = cloudinaryStorageService.uploadRaw(file);

            Documents doc = Documents.builder()
                    .documentName(name)
                    .description(description)
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .contentType(contentType)
                    .cloudinaryUrl(cloudinaryUrl)
                    .uploadedBy(uploadedBy)
                    .classId(classId)
                    .build();

            return documentRepository.save(doc);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload document: " + e.getMessage());
        }
    }

    @Override
    public List<Documents> getDocumentsByClass(String classId) {
        return documentRepository.findByClassId(classId);
    }

    @Override
    public Documents getDocumentById(String id) {
        return documentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDocument(String id) {
        Documents doc = documentRepository.findById(id).orElse(null);
        if (doc != null) {
            cloudinaryStorageService.deleteRaw(doc.getCloudinaryUrl());
            documentRepository.deleteById(id);
        }
    }
}
